package technologyforall.com.ledgerservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import technologyforall.com.ledgerservice.dto.LedgerEntryResponse;
import technologyforall.com.ledgerservice.model.LedgerEntry;
import technologyforall.com.ledgerservice.repository.LedgerEntryRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ledger")
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerEntryRepository ledgerEntryRepository;

    @GetMapping("/transfer/{transferId}")
    public ResponseEntity<List<LedgerEntryResponse>> getByTransferId(@PathVariable Long transferId) {
        return ResponseEntity.ok(toResponseList(ledgerEntryRepository.findByTransferId(transferId)));
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<LedgerEntryResponse>> getByBusinessId(@PathVariable Long businessId) {
        return ResponseEntity.ok(toResponseList(ledgerEntryRepository.findByBusinessId(businessId)));
    }

    @GetMapping("/reference/{referenceNumber}")
    public ResponseEntity<List<LedgerEntryResponse>> getByReference(@PathVariable String referenceNumber) {
        return ResponseEntity.ok(toResponseList(ledgerEntryRepository.findByReferenceNumber(referenceNumber)));
    }

    private List<LedgerEntryResponse> toResponseList(List<LedgerEntry> entries) {
        return entries.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private LedgerEntryResponse toResponse(LedgerEntry e) {
        LedgerEntryResponse r = new LedgerEntryResponse();
        r.setId(e.getId());
        r.setTransferId(e.getTransferId());
        r.setReferenceNumber(e.getReferenceNumber());
        r.setBusinessId(e.getBusinessId());
        r.setEntryType(e.getEntryType().name());
        r.setAmount(e.getAmount());
        r.setDestinationAccount(e.getDestinationAccount());
        r.setDestinationBankName(e.getDestinationBankName());
        r.setDescription(e.getDescription());
        r.setCreatedAt(e.getCreatedAt());
        return r;
    }
}
