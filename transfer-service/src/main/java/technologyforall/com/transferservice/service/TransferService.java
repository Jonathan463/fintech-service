package technologyforall.com.transferservice.service;

import technologyforall.com.transferservice.dto.TransferRequest;
import technologyforall.com.transferservice.dto.TransferResponse;

public interface TransferService {
    TransferResponse initiateTransfer(TransferRequest request);
}
