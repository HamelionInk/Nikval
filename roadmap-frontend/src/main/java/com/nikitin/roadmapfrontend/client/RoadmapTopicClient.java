package com.nikitin.roadmapfrontend.client;

import com.nikitin.roadmapfrontend.configuration.rest.RestTemplateService;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmapfrontend.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmapfrontend.dto.response.pageable.PageableRoadmapTopicResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoadmapTopicClient {

	private final RestTemplateService restTemplateService;

	public RoadmapTopicResponseDto create(RoadmapTopicRequestDto roadmapTopicRequestDto) {
		return restTemplateService.request("/roadmap-topics",
				HttpMethod.POST,
				restTemplateService.buildRequestBody(roadmapTopicRequestDto, null),
				null,
				RoadmapTopicResponseDto.class);
	}

	public RoadmapTopicResponseDto patch(Long id, RoadmapTopicRequestDto roadmapTopicRequestDto) {
		return restTemplateService.request("/roadmap-topics/{id}",
				HttpMethod.PATCH,
				restTemplateService.buildRequestBody(roadmapTopicRequestDto, null),
				Map.ofEntries(Map.entry("id", id)),
				RoadmapTopicResponseDto.class);
	}

	public RoadmapTopicResponseDto getById(Long id) {
		return restTemplateService.request("/roadmap-topics/{id}",
				HttpMethod.GET,
				restTemplateService.buildRequestBody(null, null),
				Map.ofEntries(Map.entry("id", id)),
				RoadmapTopicResponseDto.class);
	}

	public PageableRoadmapTopicResponseDto getAll(RoadmapTopicFilter roadmapTopicFilter) {
		var uriParams = new HashMap<String, Object>();
		uriParams.put("roadmapChapterIds", roadmapTopicFilter.getRoadmapChapterIds());
		uriParams.put("startWithName", roadmapTopicFilter.getStartWithName());

		return restTemplateService.request(
				"/roadmap-topics",
				HttpMethod.GET,
				restTemplateService.buildRequestBody(roadmapTopicFilter, null),
				uriParams,
				PageableRoadmapTopicResponseDto.class);
	}

	public void deleteById(Long id) {
		restTemplateService.request("/roadmap-topics/{id}",
				HttpMethod.DELETE,
				restTemplateService.buildRequestBody(null, null),
				Map.ofEntries(Map.entry("id", id)),
				null);
	}
}
