package com.nikitin.roadmaps.client;

import com.nikitin.roadmaps.config.security.KeycloakTokenService;
import com.nikitin.roadmaps.dto.request.RoadmapQuestionRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RoadmapQuestionClient extends Client {
    public RoadmapQuestionClient(RestTemplate restTemplate, KeycloakTokenService keycloakTokenService) {
        super(restTemplate, keycloakTokenService);
    }

    public ResponseEntity<String> create(RoadmapQuestionRequestDto roadmapQuestionRequestDto, Boolean notificationError) {
        return request("/roadmap-questions",
                HttpMethod.POST,
                buildRequestBody(roadmapQuestionRequestDto, null),
                notificationError);
    }

    public ResponseEntity<String> patch(Long id, RoadmapQuestionRequestDto roadmapQuestionRequestDto, Boolean notificationError) {
        return request("/roadmap-questions/" + id,
                HttpMethod.PATCH,
                buildRequestBody(roadmapQuestionRequestDto, null),
                notificationError);
    }

    public ResponseEntity<String> getById(Long id, Boolean notificationError) {
        return request("/roadmap-questions/" + id,
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> getAll(Boolean notificationError) {
        return request("/roadmap-questions",
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> getAllByTopicId(Long id, Boolean notificationError) {
        return request("/roadmap-questions/roadmap-topic/" + id,
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> deleteById(Long id, Boolean notificationError) {
        return request("/roadmap-questions/" + id,
                HttpMethod.DELETE,
                buildRequestBody(null, null),
                notificationError);
    }
}
