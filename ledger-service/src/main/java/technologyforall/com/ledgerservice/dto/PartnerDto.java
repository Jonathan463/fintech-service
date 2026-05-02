package technologyforall.com.ledgerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Local mirror of profile-service PartnerResponse.
 * Used by ProfileServiceClient when fetching partner splits.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartnerDto {
    private Long id;
    private Long businessId;
    private String businessName;
    private String name;
    private float ratio;
    private String destination_account;
    private String destination_bank_name;
}
