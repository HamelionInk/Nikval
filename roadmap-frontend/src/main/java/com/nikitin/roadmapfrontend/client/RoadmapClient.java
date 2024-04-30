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

import java.util.HashMap;
import java.util.Map;

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
                null,
                RoadmapResponseDto.class
        );
    }

    public RoadmapResponseDto patch(Long id, RoadmapRequestDto roadmapRequestDto) {
        return restTemplateService.request(
                "/roadmaps/{id}",
                HttpMethod.PATCH,
                restTemplateService.buildRequestBody(roadmapRequestDto, null),
                Map.ofEntries(Map.entry("id", id)),
                RoadmapResponseDto.class
        );
    }

    public RoadmapResponseDto getById(Long id) {
        return restTemplateService.request(
                "/roadmaps/{id}",
                HttpMethod.GET,
                restTemplateService.buildRequestBody(null, null),
                Map.ofEntries(Map.entry("id", id)),
                RoadmapResponseDto.class
        );
    }

    public PageableRoadmapResponseDto getAll(RoadmapFilter roadmapFilter) {
        var uriParams = new HashMap<String, Object>();
        uriParams.put("profileIds", roadmapFilter.getProfileIds());
        uriParams.put("ids", roadmapFilter.getIds());
        uriParams.put("startWithName", roadmapFilter.getStartWithName());
        uriParams.put("custom", roadmapFilter.getCustom());
        uriParams.put("favorite", roadmapFilter.getFavorite());

        return restTemplateService.request(
                "/roadmaps",
                HttpMethod.GET,
                restTemplateService.buildRequestBody(null, null),
                uriParams,
                PageableRoadmapResponseDto.class
        );
    }

    public String deleteById(Long id) {
        return restTemplateService.request(
                "/roadmaps/{id}",
                HttpMethod.DELETE,
                restTemplateService.buildRequestBody(null, null),
                Map.ofEntries(Map.entry("id", id)),
                String.class
        );
    }
}
