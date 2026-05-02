package technologyforall.com.transferservice.dto;

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
public class TransferResponse {
    private Long id;
    private String referenceNumber;
    private String virtualAccountNumber;
    private Long businessId;
    private String sourceAccountNumber;
    private String sourceBankName;
    private BigDecimal transferAmount;
    private String status;
    private LocalDateTime createdAt;
}
