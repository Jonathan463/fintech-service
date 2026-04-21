package technologyforall.com.profileservice.service;

import technologyforall.com.profileservice.dto.PartnerRequest;
import technologyforall.com.profileservice.dto.PartnerResponse;
import technologyforall.com.profileservice.exception.RatioLimitExceededException;
import technologyforall.com.profileservice.exception.ResourceNotFoundException;
import technologyforall.com.profileservice.model.Business;
import technologyforall.com.profileservice.model.Partner;

import java.util.List;
import java.util.stream.Collectors;

public interface PartnerService {

    PartnerResponse addPartner(Long businessId, PartnerRequest request);

    List<PartnerResponse> getPartnersByBusinessId(Long businessId);

    void deletePartner(Long partnerId);
}
