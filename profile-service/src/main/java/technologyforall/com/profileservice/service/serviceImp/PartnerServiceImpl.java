package technologyforall.com.profileservice.service.serviceImp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import technologyforall.com.profileservice.dto.PartnerRequest;
import technologyforall.com.profileservice.dto.PartnerResponse;
import technologyforall.com.profileservice.exception.RatioLimitExceededException;
import technologyforall.com.profileservice.exception.ResourceNotFoundException;
import technologyforall.com.profileservice.model.Business;
import technologyforall.com.profileservice.model.Partner;
import technologyforall.com.profileservice.repository.BusinessRepository;
import technologyforall.com.profileservice.repository.PartnerRepository;
import technologyforall.com.profileservice.service.PartnerService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;
    private final BusinessRepository businessRepository;

    public PartnerResponse addPartner(Long businessId, PartnerRequest request) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found with id: " + businessId));

        if (request.getRatio() <= 0) {
            throw new RatioLimitExceededException("Partner ratio must be greater than 0.");
        }

        float existingTotal = partnerRepository.sumRatioByBusinessId(businessId);
        float newTotal = existingTotal + request.getRatio();

        if (newTotal > 100) {
            float remaining = 100 - existingTotal;
            throw new RatioLimitExceededException(
                    String.format("Adding this partner would exceed the 100%% ratio limit for this business. " +
                            "Current allocated: %.2f%%. Remaining available: %.2f%%.", existingTotal, remaining));
        }

        Partner partner = new Partner();
        partner.setBusiness(business);
        partner.setName(request.getName());
        partner.setRatio(request.getRatio());
        partner.setDestination_account(request.getDestination_account());
        partner.setDestination_bank_name(request.getDestination_bank_name());

        Partner saved = partnerRepository.save(partner);
        return toResponse(saved);
    }

    public List<PartnerResponse> getPartnersByBusinessId(Long businessId) {
        if (!businessRepository.existsById(businessId)) {
            throw new ResourceNotFoundException("Business not found with id: " + businessId);
        }
        return partnerRepository.findByBusinessId(businessId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void deletePartner(Long partnerId) {
        if (!partnerRepository.existsById(partnerId)) {
            throw new ResourceNotFoundException("Partner not found with id: " + partnerId);
        }
        partnerRepository.deleteById(partnerId);
    }

    private PartnerResponse toResponse(Partner partner) {
        PartnerResponse response = new PartnerResponse();
        response.setId(partner.getId());
        response.setBusinessId(partner.getBusiness().getId());
        response.setBusinessName(partner.getBusiness().getName());
        response.setName(partner.getName());
        response.setRatio(partner.getRatio());
        response.setDestination_account(partner.getDestination_account());
        response.setDestination_bank_name(partner.getDestination_bank_name());
        return response;
    }
}
