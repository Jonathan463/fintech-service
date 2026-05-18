package technologyforall.com.ledgerservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import technologyforall.com.ledgerservice.dto.PartnerDto;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileServiceClient {

    private final RestClient restClient;
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Value("${services.profile-service.base-url:http://localhost:8080}")
    private String profileServiceBaseUrl;

    public List<PartnerDto> getPartners(Long businessId) {
        try {
            String token = getAccessToken();
            List<PartnerDto> partners = restClient.get()
                    .uri(profileServiceBaseUrl + "/api/businesses/{businessId}/partners", businessId)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            return partners != null ? partners : Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to fetch partners for businessId={}: {}", businessId, e.getMessage());
            return Collections.emptyList();
        }
    }

    private String getAccessToken() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("keycloak")
                .principal("ledger-service")
                .build();
        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);
        if (authorizedClient == null) {
            throw new IllegalStateException("Failed to obtain OAuth2 access token for ledger-service");
        }
        return authorizedClient.getAccessToken().getTokenValue();
    }
}
