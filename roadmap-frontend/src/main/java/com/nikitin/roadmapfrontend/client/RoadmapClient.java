package com.nikitin.roadmapfrontend.client;

import com.nikitin.roadmapfrontend.configuration.rest.RestTemplateService;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapFilter;
import com.nikitin.roadmapfrontend.dto.request.RoadmapRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapResponseDto;
import com.nikitin.roadmapfrontend.dto.response.pageable.PageableRoadmapResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoadmapClient {

    private final RestTemplateService restTemplateService;

    public RoadmapResponseDto create(RoadmapRequestDto roadmapRequestDto) {
        return restTemplateService.request(
                "/roadmaps",
                HttpMethod.POST,
                restTemplateService.buildRequestBody(roadmapRequestDto, null),
                Collections.emptyMap(),
                RoadmapResponseDto.class
        );
    }

    public RoadmapResponseDto patch(Long id, RoadmapRequestDto roadmapRequestDto) {
        return restTemplateService.request(
                "/roadmaps/{id}",
                HttpMethod.PATCH,
                restTemplateService.buildRequestBody(roadmapRequestDto, null),
                Collections.singletonMap("id", id),
                RoadmapResponseDto.class
        );
    }

    public RoadmapResponseDto getById(Long id) {
        return restTemplateService.request(
                "/roadmaps/{id}",
                HttpMethod.GET,
                restTemplateService.buildRequestBody(null, null),
                Collections.singletonMap("id", id),
                RoadmapResponseDto.class
        );
    }

    public PageableRoadmapResponseDto getAll(RoadmapFilter roadmapFilter) {
        var url = UriComponentsBuilder.fromUriString("/roadmaps");
        var params = new HashMap<String, Object>();

        generateUriParams(params, url, roadmapFilter);

        return restTemplateService.request(
                url.encode().toUriString(),
                HttpMethod.GET,
                restTemplateService.buildRequestBody(null, null),
                params,
                PageableRoadmapResponseDto.class
        );
    }

    public String deleteById(Long id) {
        return restTemplateService.request(
                "/roadmaps/{id}",
                HttpMethod.DELETE,
                restTemplateService.buildRequestBody(null, null),
                Collections.singletonMap("id", id),
                String.class
        );
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

        Optional.ofNullable(roadmapFilter.getFavorite())
                .ifPresent(favorite -> {
                    uriComponentsBuilder.queryParam("favorite", "{favorite}");
                    params.put("favorite", favorite);
                });
    }
}
