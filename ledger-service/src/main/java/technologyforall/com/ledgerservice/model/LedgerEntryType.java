package technologyforall.com.ledgerservice.model;

public enum LedgerEntryType {
    CREDIT,         // Full amount received into the virtual account
    CHARGE_DEBIT,   // Fee deducted from the received amount
    PARTNER_PAYOUT  // Net amount split distributed to each partner
}
