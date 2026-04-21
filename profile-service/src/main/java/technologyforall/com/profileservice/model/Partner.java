package technologyforall.com.profileservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="partner")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    private String name;
    private float ratio;
    private String destination_account;
    private String destination_bank_name;

}
