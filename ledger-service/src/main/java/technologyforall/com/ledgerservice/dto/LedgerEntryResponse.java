package technologyforall.com.ledgerservice.dto;

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
public class LedgerEntryResponse {
    private Long id;
    private Long transferId;
    private String referenceNumber;
    private Long businessId;
    private String entryType;
    private BigDecimal amount;
    private String destinationAccount;
    private String destinationBankName;
    private String description;
    private LocalDateTime createdAt;
}
