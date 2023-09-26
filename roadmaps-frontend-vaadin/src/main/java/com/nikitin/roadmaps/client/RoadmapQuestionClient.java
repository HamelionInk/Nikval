package com.nikitin.roadmaps.client;

import com.nikitin.roadmaps.config.security.KeycloakTokenService;
import com.nikitin.roadmaps.dto.filter.RoadmapQuestionFilter;
import com.nikitin.roadmaps.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmaps.dto.request.RoadmapQuestionRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
                Collections.EMPTY_MAP,
                notificationError);
    }

    public ResponseEntity<String> patch(Long id, RoadmapQuestionRequestDto roadmapQuestionRequestDto, Boolean notificationError) {
        return request("/roadmap-questions/{id}",
                HttpMethod.PATCH,
                buildRequestBody(roadmapQuestionRequestDto, null),
                Collections.singletonMap("id", id),
                notificationError);
    }

    public ResponseEntity<String> getById(Long id, Boolean notificationError) {
        return request("/roadmap-questions/{id}",
                HttpMethod.GET,
                buildRequestBody(null, null),
                Collections.singletonMap("id", id),
                notificationError);
    }

    public ResponseEntity<String> getAll(RoadmapQuestionFilter roadmapQuestionFilter, Boolean notificationError) {
        var url = UriComponentsBuilder.fromUriString("/roadmap-questions");
        var params = new HashMap<String, Object>();

        generateUriParams(params, url, roadmapQuestionFilter);

        return request(url.encode().toUriString(),
                HttpMethod.GET,
                buildRequestBody(null, null),
                params,
                notificationError);
    }

    public ResponseEntity<String> getAllByTopicId(Long id, Boolean notificationError) {
        return request("/roadmap-questions/roadmap-topic/{id}",
                HttpMethod.GET,
                buildRequestBody(null, null),
                Collections.singletonMap("id", id),
                notificationError);
    }

    public ResponseEntity<String> deleteById(Long id, Boolean notificationError) {
        return request("/roadmap-questions/{id}",
                HttpMethod.DELETE,
                buildRequestBody(null, null),
                Collections.singletonMap("id", id),
                notificationError);
    }

    private void generateUriParams(Map<String, Object> params, UriComponentsBuilder uriComponentsBuilder, RoadmapQuestionFilter roadmapQuestionFilter) {
        Optional.ofNullable(roadmapQuestionFilter.getIds())
                .ifPresent(ids -> ids.forEach(item -> {
                    uriComponentsBuilder.queryParam("ids", "{ids}");
                    params.put("ids", item);
                }));

        Optional.ofNullable(roadmapQuestionFilter.getRoadmapTopicIds())
                .ifPresent(roadmapTopicIds -> roadmapTopicIds.forEach(item -> {
                    uriComponentsBuilder.queryParam("roadmapTopicIds", "{roadmapTopicIds}");
                    params.put("roadmapTopicIds", item);
                }));

        Optional.ofNullable(roadmapQuestionFilter.getStartWithQuestion())
                .ifPresent(startWithQuestion -> {
                    uriComponentsBuilder.queryParam("startWithQuestion", "{startWithQuestion}");
                    params.put("startWithQuestion", startWithQuestion);
                });

        Optional.ofNullable(roadmapQuestionFilter.getStartWithAnswer())
                .ifPresent(startWithAnswer -> {
                    uriComponentsBuilder.queryParam("startWithAnswer", "{startWithAnswer}");
                    params.put("startWithAnswer", startWithAnswer);
                });

        Optional.ofNullable(roadmapQuestionFilter.getIsExplored())
                .ifPresent(isExplored -> {
                    uriComponentsBuilder.queryParam("isExplored", "{isExplored}");
                    params.put("isExplored", isExplored);
                });
    }
}
