package technologyforall.com.virtualaccountservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import technologyforall.com.virtualaccountservice.dto.VirtualAccountRequest;
import technologyforall.com.virtualaccountservice.dto.VirtualAccountResponse;
import technologyforall.com.virtualaccountservice.service.serviceImpl.VirtualAccountServiceImpl;

@RestController
@RequestMapping("/api/virtual-accounts")
@RequiredArgsConstructor
public class VirtualAccountController {

    private final VirtualAccountServiceImpl virtualAccountServiceImpl;

    @PostMapping
    public ResponseEntity<VirtualAccountResponse> createVirtualAccount(
            @RequestBody VirtualAccountRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(virtualAccountServiceImpl.createVirtualAccount(request));
    }
}
