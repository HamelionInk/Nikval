package com.nikitin.roadmapfrontend.client;

import com.nikitin.roadmapfrontend.configuration.rest.RestTemplateService;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmapfrontend.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmapfrontend.dto.response.pageable.PageableRoadmapTopicResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoadmapTopicClient {

	private final RestTemplateService restTemplateService;

	public RoadmapTopicResponseDto create(RoadmapTopicRequestDto roadmapTopicRequestDto) {
		return restTemplateService.request("/roadmap-topics",
				HttpMethod.POST,
				restTemplateService.buildRequestBody(roadmapTopicRequestDto, null),
				Collections.EMPTY_MAP,
				RoadmapTopicResponseDto.class);
	}

	public RoadmapTopicResponseDto patch(Long id, RoadmapTopicRequestDto roadmapTopicRequestDto) {
		return restTemplateService.request("/roadmap-topics/{id}",
				HttpMethod.PATCH,
				restTemplateService.buildRequestBody(roadmapTopicRequestDto, null),
				Collections.singletonMap("id", id),
				RoadmapTopicResponseDto.class);
	}

	public RoadmapTopicResponseDto getById(Long id) {
		return restTemplateService.request("/roadmap-topics/{id}",
				HttpMethod.GET,
				restTemplateService.buildRequestBody(null, null),
				Collections.singletonMap("id", id),
				RoadmapTopicResponseDto.class);
	}

	public PageableRoadmapTopicResponseDto getAll(RoadmapTopicFilter roadmapTopicFilter) {
		var url = UriComponentsBuilder.fromUriString("/roadmap-topics");
		var params = new HashMap<String, Object>();

		generateUriParams(params, url, roadmapTopicFilter);

		return restTemplateService.request(url.encode().toUriString(),
				HttpMethod.GET,
				restTemplateService.buildRequestBody(roadmapTopicFilter, null),
				params,
				PageableRoadmapTopicResponseDto.class);
	}

	public void deleteById(Long id) {
		restTemplateService.request("/roadmap-topics/{id}",
				HttpMethod.DELETE,
				restTemplateService.buildRequestBody(null, null),
				Collections.singletonMap("id", id),
				null);
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
