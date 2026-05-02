package technologyforall.com.ledgerservice.service;

import technologyforall.com.ledgerservice.event.ChargeCalculatedEvent;

public interface LedgerService {
    int recordEntries(ChargeCalculatedEvent event);
}
