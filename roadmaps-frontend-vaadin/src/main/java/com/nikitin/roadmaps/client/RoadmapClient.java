package com.nikitin.roadmaps.client;

import com.nikitin.roadmaps.config.security.KeycloakTokenService;
import com.nikitin.roadmaps.dto.filter.RoadmapFilter;
import com.nikitin.roadmaps.dto.request.RoadmapRequestDto;
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
public class RoadmapClient extends Client {
    public RoadmapClient(RestTemplate restTemplate, KeycloakTokenService keycloakTokenService) {
        super(restTemplate, keycloakTokenService);
    }

    public ResponseEntity<String> create(RoadmapRequestDto roadmapRequestDto, Boolean notificationError) {
        return request("/roadmaps",
                HttpMethod.POST,
                buildRequestBody(roadmapRequestDto, null),
                Collections.EMPTY_MAP,
                notificationError);
    }

    public ResponseEntity<String> patch(Long id, RoadmapRequestDto roadmapRequestDto, Boolean notificationError) {
        return request("/roadmaps/{id}",
                HttpMethod.PATCH,
                buildRequestBody(roadmapRequestDto, null),
                Collections.singletonMap("id", id),
                notificationError);
    }

    public ResponseEntity<String> getById(Long id, Boolean notificationError) {
        return request("/roadmaps/{id}",
                HttpMethod.GET,
                buildRequestBody(null, null),
                Collections.singletonMap("id", id),
                notificationError);
    }

    public ResponseEntity<String> getAll(RoadmapFilter roadmapFilter, Boolean notificationError) {
        var url = UriComponentsBuilder.fromUriString("/roadmaps");
        var params = new HashMap<String, Object>();

        generateUriParams(params, url, roadmapFilter);

        return request(url.encode().toUriString(),
                HttpMethod.GET,
                buildRequestBody(null, null),
                params,
                notificationError);
    }

    public ResponseEntity<String> getAllByProfileId(Long id, Boolean notificationError) {
        return request("/roadmaps/profile/{id}",
                HttpMethod.GET,
                buildRequestBody(null, null),
                Collections.singletonMap("id", id),
                notificationError);
    }

    public ResponseEntity<String> deleteById(Long id, Boolean notificationError) {
        return request("/roadmaps/{id}",
                HttpMethod.DELETE,
                buildRequestBody(null, null),
                Collections.singletonMap("id", id),
                notificationError);
    }

    private void generateUriParams(Map<String, Object> params, UriComponentsBuilder uriComponentsBuilder, RoadmapFilter roadmapFilter) {
        Optional.ofNullable(roadmapFilter.getProfileIds())
                .ifPresent(profileIds -> profileIds.forEach(item -> {
                    uriComponentsBuilder.queryParam("profileIds", "{profileIds}");
                    params.put("profileIds", item);
                }));

        Optional.ofNullable(roadmapFilter.getIds())
                .ifPresent(ids -> ids.forEach(item -> {
                    uriComponentsBuilder.queryParam("ids", "{ids}");
                    params.put("ids", item);
                }));

        Optional.ofNullable(roadmapFilter.getStartWithName())
                .ifPresent(startWithName -> {
                    uriComponentsBuilder.queryParam("startWithName", "{startWithName}");
                    params.put("startWithName", startWithName);
                });

        Optional.ofNullable(roadmapFilter.getCustom())
                .ifPresent(custom -> {
                    uriComponentsBuilder.queryParam("custom", "{custom}");
                    params.put("custom", custom);
                });
    }

}
