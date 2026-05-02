package technologyforall.com.chargeservice.event;

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
public class TransferInitiatedEvent {
    private Long transferId;
    private String referenceNumber;
    private String virtualAccountNumber;
    private Long businessId;
    private String sourceAccountNumber;
    private String sourceBankName;
    private BigDecimal transferAmount;
    private LocalDateTime createdAt;
}
