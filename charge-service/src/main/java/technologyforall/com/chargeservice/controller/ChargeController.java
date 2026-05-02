package technologyforall.com.chargeservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import technologyforall.com.chargeservice.dto.ChargeRequest;
import technologyforall.com.chargeservice.dto.ChargeResponse;
import technologyforall.com.chargeservice.service.ChargeServiceImpl;

@RestController
@RequestMapping("/api/charges")
@Slf4j
@RequiredArgsConstructor
public class ChargeController {

    private final ChargeServiceImpl chargeServiceImpl;

    @PostMapping
    public ResponseEntity<ChargeResponse> calculateCharge(@RequestBody ChargeRequest request) {
        log.info("got to the charge controller");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(chargeServiceImpl.calculateAndSaveCharge(request));
    }
}
