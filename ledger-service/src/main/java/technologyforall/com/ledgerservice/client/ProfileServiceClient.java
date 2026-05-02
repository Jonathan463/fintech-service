package technologyforall.com.ledgerservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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

    @Value("${services.profile-service.base-url:http://localhost:8081}")
    private String profileServiceBaseUrl;

    public List<PartnerDto> getPartners(Long businessId) {
        try {
            List<PartnerDto> partners = restClient.get()
                    .uri(profileServiceBaseUrl + "/api/businesses/{businessId}/partners", businessId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            return partners != null ? partners : Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to fetch partners for businessId={}: {}", businessId, e.getMessage());
            return Collections.emptyList();
        }
    }
}
