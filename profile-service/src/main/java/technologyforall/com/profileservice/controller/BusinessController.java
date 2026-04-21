package technologyforall.com.profileservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import technologyforall.com.profileservice.dto.BusinessRequest;
import technologyforall.com.profileservice.dto.BusinessResponse;
import technologyforall.com.profileservice.service.serviceImp.BusinessServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessServiceImpl businessServiceImpl;

    @PostMapping
    public ResponseEntity<BusinessResponse> addBusiness(@RequestBody BusinessRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(businessServiceImpl.addBusiness(request));
    }

    @GetMapping
    public ResponseEntity<List<BusinessResponse>> getAllBusinesses() {
        return ResponseEntity.ok(businessServiceImpl.getAllBusinesses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessResponse> getBusinessById(@PathVariable Long id) {
        return ResponseEntity.ok(businessServiceImpl.getBusinessById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable Long id) {
        businessServiceImpl.deleteBusiness(id);
        return ResponseEntity.noContent().build();
    }
}
