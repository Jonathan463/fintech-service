package technologyforall.com.transferservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import technologyforall.com.transferservice.dto.TransferRequest;
import technologyforall.com.transferservice.dto.TransferResponse;
import technologyforall.com.transferservice.service.TransferService;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> initiateTransfer(@RequestBody TransferRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(transferService.initiateTransfer(request));
    }
}
