package technologyforall.com.ledgerservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ledger_entry")
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long transferId;
    private String referenceNumber;
    private Long businessId;

    @Enumerated(EnumType.STRING)
    private LedgerEntryType entryType;

    private BigDecimal amount;

    // Destination of funds (virtual account for CREDIT, partner account for PARTNER_PAYOUT, null for CHARGE_DEBIT)
    private String destinationAccount;
    private String destinationBankName;

    private String description;
    private LocalDateTime createdAt;
}
