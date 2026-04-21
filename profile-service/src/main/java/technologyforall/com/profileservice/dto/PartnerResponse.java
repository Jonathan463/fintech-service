package technologyforall.com.profileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartnerResponse {
    private Long id;
    private Long businessId;
    private String businessName;
    private String name;
    private float ratio;
    private String destination_account;
    private String destination_bank_name;
}
