package technologyforall.com.transferservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    private String virtualAccountNumber;
    private Long businessId;
    private String sourceAccountNumber;
    private String sourceBankName;
    private BigDecimal transferAmount;
}
