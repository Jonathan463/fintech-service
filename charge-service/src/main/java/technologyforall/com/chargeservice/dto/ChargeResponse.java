package technologyforall.com.chargeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChargeResponse {
    private Long id;
    private String sourceAcct;
    private String destAcct;
    private String sourceBankName;
    private BigDecimal transferAmount;
    private String chargeRate;
    private BigDecimal charge;
    private BigDecimal totalDebit;
}
