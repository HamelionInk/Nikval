package com.nikitin.roadmaps.client;

import com.nikitin.roadmaps.config.security.KeycloakTokenService;
import com.nikitin.roadmaps.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmaps.dto.request.RoadmapTopicRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RoadmapTopicClient extends Client{
    public RoadmapTopicClient(RestTemplate restTemplate, KeycloakTokenService keycloakTokenService) {
        super(restTemplate, keycloakTokenService);
    }

    public ResponseEntity<String> create(RoadmapTopicRequestDto roadmapTopicRequestDto, Boolean notificationError) {
        return request("/roadmap-topics",
                HttpMethod.POST,
                buildRequestBody(roadmapTopicRequestDto, null),
                notificationError);
    }

    public ResponseEntity<String> patch(Long id, RoadmapTopicRequestDto roadmapTopicRequestDto, Boolean notificationError) {
        return request("/roadmap-topics/" + id,
                HttpMethod.PATCH,
                buildRequestBody(roadmapTopicRequestDto, null),
                notificationError);
    }

    public ResponseEntity<String> getById(Long id, Boolean notificationError) {
        return request("/roadmap-topics/" + id,
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> getAll(Boolean notificationError) {
        return request("/roadmap-topics",
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> getAllByChapterId(Long id, Boolean notificationError) {
        return request("/roadmap-topics/roadmap-chapter/" + id,
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> deleteById(Long id, Boolean notificationError) {
        return request("/roadmap-topics/" + id,
                HttpMethod.DELETE,
                buildRequestBody(null, null),
                notificationError);
    }
}
