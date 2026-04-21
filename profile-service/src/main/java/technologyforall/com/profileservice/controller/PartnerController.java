package technologyforall.com.profileservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import technologyforall.com.profileservice.dto.PartnerRequest;
import technologyforall.com.profileservice.dto.PartnerResponse;
import technologyforall.com.profileservice.service.serviceImp.PartnerServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerServiceImpl partnerServiceImpl;

    @PostMapping("/{businessId}/partners")
    public ResponseEntity<PartnerResponse> addPartner(
            @PathVariable Long businessId,
            @RequestBody PartnerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partnerServiceImpl.addPartner(businessId, request));
    }

    @GetMapping("/{businessId}/partners")
    public ResponseEntity<List<PartnerResponse>> getPartnersByBusinessId(@PathVariable Long businessId) {
        return ResponseEntity.ok(partnerServiceImpl.getPartnersByBusinessId(businessId));
    }

    @DeleteMapping("/partners/{partnerId}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long partnerId) {
        partnerServiceImpl.deletePartner(partnerId);
        return ResponseEntity.noContent().build();
    }
}
