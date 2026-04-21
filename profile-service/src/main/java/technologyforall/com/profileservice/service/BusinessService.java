package technologyforall.com.profileservice.service;

import technologyforall.com.profileservice.dto.BusinessRequest;
import technologyforall.com.profileservice.dto.BusinessResponse;
import technologyforall.com.profileservice.exception.ResourceNotFoundException;
import technologyforall.com.profileservice.model.Business;

import java.util.List;
import java.util.stream.Collectors;

public interface BusinessService {
    BusinessResponse addBusiness(BusinessRequest request);

    List<BusinessResponse> getAllBusinesses();

    BusinessResponse getBusinessById(Long id);

    void deleteBusiness(Long id);
}
