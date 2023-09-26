package com.nikitin.roadmaps.client;

import com.nikitin.roadmaps.config.security.KeycloakTokenService;
import com.nikitin.roadmaps.dto.filter.RoadmapFilter;
import com.nikitin.roadmaps.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmaps.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmaps.dto.request.RoadmapTopicRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class RoadmapTopicClient extends Client {
    public RoadmapTopicClient(RestTemplate restTemplate, KeycloakTokenService keycloakTokenService) {
        super(restTemplate, keycloakTokenService);
    }

    public ResponseEntity<String> create(RoadmapTopicRequestDto roadmapTopicRequestDto, Boolean notificationError) {
        return request("/roadmap-topics",
                HttpMethod.POST,
                buildRequestBody(roadmapTopicRequestDto, null),
                Collections.EMPTY_MAP,
                notificationError);
    }

    public ResponseEntity<String> patch(Long id, RoadmapTopicRequestDto roadmapTopicRequestDto, Boolean notificationError) {
        return request("/roadmap-topics/{id}",
                HttpMethod.PATCH,
                buildRequestBody(roadmapTopicRequestDto, null),
                Collections.singletonMap("id", id),
                notificationError);
    }

    public ResponseEntity<String> getById(Long id, Boolean notificationError) {
        return request("/roadmap-topics/{id}",
                HttpMethod.GET,
                buildRequestBody(null, null),
                Collections.singletonMap("id", id),
                notificationError);
    }

    public ResponseEntity<String> getAll(RoadmapTopicFilter roadmapTopicFilter, Boolean notificationError) {
        var url = UriComponentsBuilder.fromUriString("/roadmap-topics");
        var params = new HashMap<String, Object>();

        generateUriParams(params, url, roadmapTopicFilter);

        return request(url.encode().toUriString(),
                HttpMethod.GET,
                buildRequestBody(roadmapTopicFilter, null),
                params,
                notificationError);
    }

    public ResponseEntity<String> getAllByChapterId(Long id, Boolean notificationError) {
        return request("/roadmap-topics/roadmap-chapter/{id}",
                HttpMethod.GET,
                buildRequestBody(null, null),
                Collections.singletonMap("id", id),
                notificationError);
    }

    public ResponseEntity<String> deleteById(Long id, Boolean notificationError) {
        return request("/roadmap-topics/{id}",
                HttpMethod.DELETE,
                buildRequestBody(null, null),
                Collections.singletonMap("id", id),
                notificationError);
    }

    private void generateUriParams(Map<String, Object> params, UriComponentsBuilder uriComponentsBuilder, RoadmapTopicFilter roadmapTopicFilter) {
        Optional.ofNullable(roadmapTopicFilter.getRoadmapChapterIds())
                .ifPresent(roadmapChapterIds -> roadmapChapterIds.forEach(item -> {
                    uriComponentsBuilder.queryParam("roadmapChapterIds", "{roadmapChapterIds}");
                    params.put("roadmapChapterIds", item);
                }));

        Optional.ofNullable(roadmapTopicFilter.getStartWithName())
                .ifPresent(startWithName -> {
                    uriComponentsBuilder.queryParam("startWithName", "{startWithName}");
                    params.put("startWithName", startWithName);
                });
    }
}
