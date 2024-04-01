package com.nikitin.roadmapfrontend.client;

import com.nikitin.roadmapfrontend.configuration.rest.RestTemplateService;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapChapterFilter;
import com.nikitin.roadmapfrontend.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmapfrontend.dto.response.pageable.PageableRoadmapChapterResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoadmapChapterClient {

	private final RestTemplateService restTemplateService;

	public RoadmapChapterResponseDto create(RoadmapChapterRequestDto roadmapChapterRequestDto) {
		return restTemplateService.request("/roadmap-chapters",
				HttpMethod.POST,
				restTemplateService.buildRequestBody(roadmapChapterRequestDto, null),
				null,
				RoadmapChapterResponseDto.class
		);
	}

	public RoadmapChapterResponseDto patch(Long id, RoadmapChapterRequestDto roadmapChapterRequestDto) {
		return restTemplateService.request("/roadmap-chapters/{id}",
				HttpMethod.PATCH,
				restTemplateService.buildRequestBody(roadmapChapterRequestDto, null),
				Map.ofEntries(Map.entry("id", id)),
				RoadmapChapterResponseDto.class
		);
	}

	public RoadmapChapterResponseDto getById(Long id) {
		return restTemplateService.request("/roadmap-chapters/{id}",
				HttpMethod.GET,
				restTemplateService.buildRequestBody(null, null),
				Map.ofEntries(Map.entry("id", id)),
				RoadmapChapterResponseDto.class);
	}

	public PageableRoadmapChapterResponseDto getAll(RoadmapChapterFilter roadmapChapterFilter) {
		var uriParams = new HashMap<String, Object>();
		uriParams.put("ids", roadmapChapterFilter.getIds());
		uriParams.put("roadmapIds", roadmapChapterFilter.getRoadmapIds());
		uriParams.put("startWithName", roadmapChapterFilter.getStartWithName());
		uriParams.put("page", roadmapChapterFilter.getPage());
		uriParams.put("size", roadmapChapterFilter.getSize());

		return restTemplateService.request(
				"/roadmap-chapters",
				HttpMethod.GET,
				restTemplateService.buildRequestBody(null, null),
				uriParams,
				PageableRoadmapChapterResponseDto.class);
	}

	public void deleteById(Long id) {
		restTemplateService.request("/roadmap-chapters/{id}",
				HttpMethod.DELETE,
				restTemplateService.buildRequestBody(null, null),
				Map.ofEntries(Map.entry("id", id)),
				Void.class);
	}
}
