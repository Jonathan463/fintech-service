package technologyforall.com.ledgerservice.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChargeCalculatedEvent {
    private Long transferId;
    private String referenceNumber;
    private Long businessId;
    private String virtualAccountNumber;
    private String sourceAccountNumber;
    private String sourceBankName;
    private BigDecimal transferAmount;
    private BigDecimal chargeAmount;
    private String chargeRate;
    private BigDecimal netAmount;
}
