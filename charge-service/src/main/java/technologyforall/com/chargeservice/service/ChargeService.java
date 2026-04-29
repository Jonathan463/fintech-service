package technologyforall.com.chargeservice.service;

import technologyforall.com.chargeservice.dto.ChargeRequest;
import technologyforall.com.chargeservice.dto.ChargeResponse;


public interface ChargeService {
    public ChargeResponse calculateAndSaveCharge(ChargeRequest request);
}
