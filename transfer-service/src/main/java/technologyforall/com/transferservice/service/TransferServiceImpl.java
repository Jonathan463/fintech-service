package technologyforall.com.transferservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import technologyforall.com.transferservice.dto.TransferRequest;
import technologyforall.com.transferservice.dto.TransferResponse;
import technologyforall.com.transferservice.event.TransferInitiatedEvent;
import technologyforall.com.transferservice.exception.DuplicateTransferException;
import technologyforall.com.transferservice.exception.InvalidTransferException;
import technologyforall.com.transferservice.kafka.TransferEventProducer;
import technologyforall.com.transferservice.model.Transfer;
import technologyforall.com.transferservice.model.TransferStatus;
import technologyforall.com.transferservice.repository.TransferRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final TransferEventProducer transferEventProducer;
    private static final AtomicLong counter = new AtomicLong(1);

    @Override
    public TransferResponse initiateTransfer(TransferRequest request) {
        if (request.getTransferAmount() == null || request.getTransferAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferException("Transfer amount must be greater than zero.");
        }
        if (request.getVirtualAccountNumber() == null || request.getVirtualAccountNumber().length() != 10) {
            throw new InvalidTransferException("Virtual account number must be exactly 10 digits.");
        }

        String reference = generateReference();

        Transfer transfer = new Transfer();
        transfer.setReferenceNumber(reference);
        transfer.setVirtualAccountNumber(request.getVirtualAccountNumber());
        transfer.setBusinessId(request.getBusinessId());
        transfer.setSourceAccountNumber(request.getSourceAccountNumber());
        transfer.setSourceBankName(request.getSourceBankName());
        transfer.setTransferAmount(request.getTransferAmount());
        transfer.setStatus(TransferStatus.PENDING);
        transfer.setCreatedAt(LocalDateTime.now());
        transfer.setUpdatedAt(LocalDateTime.now());

        if(transferRepository.findByReferenceNumber(reference).isPresent()){
            throw new DuplicateTransferException("A transfer with number " + reference + "already exist");
        }

        Transfer saved = transferRepository.save(transfer);


            TransferInitiatedEvent event = new TransferInitiatedEvent(
                    saved.getId(), saved.getReferenceNumber(), saved.getVirtualAccountNumber(),
                    saved.getBusinessId(), saved.getSourceAccountNumber(), saved.getSourceBankName(),
                    saved.getTransferAmount(), saved.getCreatedAt());
            try{
            transferEventProducer.publishTransferInitiated(event);
        }
        catch(Exception e){
            log.error("Failed to publish transfer initiated event: {}", event, e);
            throw e;
        }

            log.info("Saved transfer: id={}, status={} ",saved.getId(), saved.getStatus().name());
        return toResponse(saved);
    }

    private String generateReference() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "TXN-" + date + "-" + String.format("%05d", counter.getAndIncrement());
    }

    private TransferResponse toResponse(Transfer t) {
        TransferResponse r = new TransferResponse();
        r.setId(t.getId());
        r.setReferenceNumber(t.getReferenceNumber());
        r.setVirtualAccountNumber(t.getVirtualAccountNumber());
        r.setBusinessId(t.getBusinessId());
        r.setSourceAccountNumber(t.getSourceAccountNumber());
        r.setSourceBankName(t.getSourceBankName());
        r.setTransferAmount(t.getTransferAmount());
        r.setStatus(t.getStatus().name());
        r.setCreatedAt(t.getCreatedAt());
        return r;
    }
}
