package technologyforall.com.notificationservice.event;

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
public class TransferCompletedEvent {
    private Long transferId;
    private String referenceNumber;
    private Long businessId;
    private String virtualAccountNumber;
    private BigDecimal transferAmount;
    private BigDecimal chargeAmount;
    private BigDecimal netAmount;
    private String status;
    private LocalDateTime completedAt;
}
