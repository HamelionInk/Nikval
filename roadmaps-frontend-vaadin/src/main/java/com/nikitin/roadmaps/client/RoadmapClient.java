package com.nikitin.roadmaps.client;

import com.nikitin.roadmaps.config.security.KeycloakTokenService;
import com.nikitin.roadmaps.dto.request.RoadmapRequestDto;
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

    public ResponseEntity<String> patch(Long id, RoadmapRequestDto roadmapRequestDto, Boolean notificationError) {
        return request("/roadmaps/" + id,
                HttpMethod.PATCH,
                buildRequestBody(roadmapRequestDto, null),
                notificationError);
    }

    public ResponseEntity<String> getById(Long id, Boolean notificationError) {
        return request("/roadmaps/" + id,
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> getAll(Boolean notificationError) {
        return request("/roadmaps",
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> getAllByProfileId(Long id, Boolean notificationError) {
        return request("/roadmaps/profile/" + id,
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> deleteById(Long id, Boolean notificationError) {
        return request("/roadmaps/" + id,
                HttpMethod.DELETE,
                buildRequestBody(null, null),
                notificationError);
    }
}
