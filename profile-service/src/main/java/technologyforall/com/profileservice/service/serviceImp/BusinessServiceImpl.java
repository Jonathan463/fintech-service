package technologyforall.com.profileservice.service.serviceImp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import technologyforall.com.profileservice.dto.BusinessRequest;
import technologyforall.com.profileservice.dto.BusinessResponse;
import technologyforall.com.profileservice.exception.ResourceNotFoundException;
import technologyforall.com.profileservice.model.Business;
import technologyforall.com.profileservice.repository.BusinessRepository;
import technologyforall.com.profileservice.service.BusinessService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;

    public BusinessResponse addBusiness(BusinessRequest request) {
        Business business = new Business();
        business.setName(request.getName());
        business.setVirtualId(request.getVirtualId());
        Business saved = businessRepository.save(business);
        return toResponse(saved);
    }

    public List<BusinessResponse> getAllBusinesses() {
        return businessRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BusinessResponse getBusinessById(Long id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found with id: " + id));
        return toResponse(business);
    }

    public void deleteBusiness(Long id) {
        if (!businessRepository.existsById(id)) {
            throw new ResourceNotFoundException("Business not found with id: " + id);
        }
        businessRepository.deleteById(id);
    }

    private BusinessResponse toResponse(Business business) {
        BusinessResponse response = new BusinessResponse();
        response.setId(business.getId());
        response.setName(business.getName());
        response.setVirtualId(business.getVirtualId());
        return response;
    }
}
