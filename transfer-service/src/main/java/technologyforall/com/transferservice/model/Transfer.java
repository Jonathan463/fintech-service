package technologyforall.com.transferservice.model;

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
@Table(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String referenceNumber;

    private String virtualAccountNumber;
    private Long businessId;
    private String sourceAccountNumber;
    private String sourceBankName;

    private BigDecimal transferAmount;
    private BigDecimal chargeAmount;
    private BigDecimal netAmount;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    private String failureReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
