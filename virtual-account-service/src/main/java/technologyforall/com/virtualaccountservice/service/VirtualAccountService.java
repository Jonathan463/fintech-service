package technologyforall.com.virtualaccountservice.service;

import technologyforall.com.virtualaccountservice.dto.VirtualAccountRequest;
import technologyforall.com.virtualaccountservice.dto.VirtualAccountResponse;
import technologyforall.com.virtualaccountservice.exception.InvalidBusinessIdException;
import technologyforall.com.virtualaccountservice.model.VirtualAccount;

import java.util.concurrent.ThreadLocalRandom;

public interface VirtualAccountService {

    VirtualAccountResponse createVirtualAccount(VirtualAccountRequest request);
}
