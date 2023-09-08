package com.nikitin.roadmaps.client;

import com.nikitin.roadmaps.config.security.KeycloakTokenService;
import com.nikitin.roadmaps.dto.request.RoadmapRequestDto;
import com.nikitin.roadmaps.dto.response.RoadmapResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RoadmapClient extends Client {
    public RoadmapClient(RestTemplate restTemplate, KeycloakTokenService keycloakTokenService) {
        super(restTemplate, keycloakTokenService);
    }

    public ResponseEntity<String> create(RoadmapRequestDto roadmapRequestDto, Boolean notificationError) {
        return request("/roadmaps",
                HttpMethod.POST,
                buildRequestBody(roadmapRequestDto, null),
                notificationError);
    }

    public ResponseEntity<String> getAllByProfileId(Long id, Boolean notificationError) {
        return request("/roadmaps/profile/" + id,
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }
}
