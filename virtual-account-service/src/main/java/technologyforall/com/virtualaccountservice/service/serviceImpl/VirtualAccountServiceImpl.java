package technologyforall.com.virtualaccountservice.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import technologyforall.com.virtualaccountservice.dto.VirtualAccountRequest;
import technologyforall.com.virtualaccountservice.dto.VirtualAccountResponse;
import technologyforall.com.virtualaccountservice.exception.InvalidBusinessIdException;
import technologyforall.com.virtualaccountservice.model.VirtualAccount;
import technologyforall.com.virtualaccountservice.repository.VirtualAccountRepository;
import technologyforall.com.virtualaccountservice.service.VirtualAccountService;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class VirtualAccountServiceImpl implements VirtualAccountService {

    private final VirtualAccountRepository virtualAccountRepository;

    public VirtualAccountResponse createVirtualAccount(VirtualAccountRequest request) {
        Long businessId = request.getBusinessId();

        // Validate businessId is exactly 4 digits (1000–9999)
        if (businessId == null || businessId < 1000 || businessId > 9999) {
            throw new InvalidBusinessIdException(
                    "Business ID must be exactly 4 digits (1000–9999). Provided: " + businessId);
        }

        // Generate a unique 10-digit virtual account: first 4 digits = businessId, last 6 = random
        String virtualAccountNumber = generateUniqueVirtualAccount(businessId);

        VirtualAccount account = new VirtualAccount();
        account.setBusinessId(businessId);
        account.setVirtualAccount(virtualAccountNumber);

        VirtualAccount saved = virtualAccountRepository.save(account);
        return toResponse(saved);
    }

    private String generateUniqueVirtualAccount(Long businessId) {
        String candidate;
        do {
            // Random suffix: 000000 to 999999 (6 digits, zero-padded)
            int suffix = ThreadLocalRandom.current().nextInt(0, 1_000_000);
            candidate = String.format("%04d%06d", businessId, suffix);
        } while (virtualAccountRepository.existsByVirtualAccount(candidate));

        return candidate;
    }

    private VirtualAccountResponse toResponse(VirtualAccount account) {
        VirtualAccountResponse response = new VirtualAccountResponse();
        response.setId(account.getId());
        response.setBusinessId(account.getBusinessId());
        response.setVirtualAccount(account.getVirtualAccount());
        return response;
    }
}
