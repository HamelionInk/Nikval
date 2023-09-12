package com.nikitin.roadmaps.client;

import com.nikitin.roadmaps.config.security.KeycloakTokenService;
import com.nikitin.roadmaps.dto.request.RoadmapChapterRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RoadmapChapterClient extends Client {
    public RoadmapChapterClient(RestTemplate restTemplate, KeycloakTokenService keycloakTokenService) {
        super(restTemplate, keycloakTokenService);
    }

    public ResponseEntity<String> create(RoadmapChapterRequestDto roadmapChapterRequestDto, Boolean notificationError) {
        return request("/roadmap-chapters",
                HttpMethod.POST,
                buildRequestBody(roadmapChapterRequestDto, null),
                notificationError);
    }

    public ResponseEntity<String> patch(Long id, RoadmapChapterRequestDto roadmapChapterRequestDto, Boolean notificationError) {
        return request("/roadmap-chapters/" + id,
                HttpMethod.PATCH,
                buildRequestBody(roadmapChapterRequestDto, null),
                notificationError);
    }

    public ResponseEntity<String> getById(Long id, Boolean notificationError) {
        return request("/roadmap-chapters/" + id,
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> getAll(Boolean notificationError) {
        return request("/roadmap-chapters",
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> getAllByRoadmapId(Long id, Boolean notificationError) {
        return request("/roadmap-chapters/roadmap/" + id,
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> deleteById(Long id, Boolean notificationError) {
        return request("/roadmap-chapters/" + id,
                HttpMethod.DELETE,
                buildRequestBody(null, null),
                notificationError);
    }
}
