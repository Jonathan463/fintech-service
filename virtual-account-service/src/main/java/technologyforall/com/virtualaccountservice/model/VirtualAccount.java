package technologyforall.com.virtualaccountservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "virtual_account")
public class VirtualAccount {
    @Id
    private Long id;
    private Long businessId;
    private String virtualAccount;
}
