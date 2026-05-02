package technologyforall.com.chargeservice.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "charge", uniqueConstraints = @UniqueConstraint(columnNames = "transfer_id"))
public class Charge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transfer_id", unique = true)
    private Long transferId;
    private BigDecimal transferAmount;
    private BigDecimal charge;
    private String sourceAcct;
    private String destAcct;
    private String sourceBankName;
}
