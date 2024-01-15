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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoadmapChapterClient {

	private final RestTemplateService restTemplateService;

	public RoadmapChapterResponseDto create(RoadmapChapterRequestDto roadmapChapterRequestDto) {
		return restTemplateService.request("/roadmap-chapters",
				HttpMethod.POST,
				restTemplateService.buildRequestBody(roadmapChapterRequestDto, null),
				Collections.EMPTY_MAP,
				RoadmapChapterResponseDto.class
		);
	}

	public RoadmapChapterResponseDto patch(Long id, RoadmapChapterRequestDto roadmapChapterRequestDto) {
		return restTemplateService.request("/roadmap-chapters/{id}",
				HttpMethod.PATCH,
				restTemplateService.buildRequestBody(roadmapChapterRequestDto, null),
				Collections.singletonMap("id", id),
				RoadmapChapterResponseDto.class
		);
	}

	public RoadmapChapterResponseDto getById(Long id) {
		return restTemplateService.request("/roadmap-chapters/{id}",
				HttpMethod.GET,
				restTemplateService.buildRequestBody(null, null),
				Collections.singletonMap("id", id),
				RoadmapChapterResponseDto.class);
	}

	public PageableRoadmapChapterResponseDto getAll(RoadmapChapterFilter roadmapChapterFilter) {
		var url = UriComponentsBuilder.fromUriString("/roadmap-chapters");
		var params = new HashMap<String, Object>();

		generateUriParams(params, url, roadmapChapterFilter);

		return restTemplateService.request(
				url.encode().toUriString(),
				HttpMethod.GET,
				restTemplateService.buildRequestBody(null, null),
				params,
				PageableRoadmapChapterResponseDto.class);
	}

	public void deleteById(Long id) {
		restTemplateService.request("/roadmap-chapters/{id}",
				HttpMethod.DELETE,
				restTemplateService.buildRequestBody(null, null),
				Collections.singletonMap("id", id),
				Void.class);
	}

	private void generateUriParams(Map<String, Object> params, UriComponentsBuilder uriComponentsBuilder, RoadmapChapterFilter roadmapChapterFilter) {
		Optional.ofNullable(roadmapChapterFilter.getIds())
				.ifPresent(ids -> ids.forEach(item -> {
					uriComponentsBuilder.queryParam("ids", "{ids}");
					params.put("ids", item);
				}));

		Optional.ofNullable(roadmapChapterFilter.getRoadmapIds())
				.ifPresent(roadmapIds -> roadmapIds.forEach(item -> {
					uriComponentsBuilder.queryParam("roadmapIds", "{roadmapIds}");
					params.put("roadmapIds", item);
				}));

		Optional.ofNullable(roadmapChapterFilter.getStartWithName())
				.ifPresent(startWithName -> {
					uriComponentsBuilder.queryParam("startWithName", "{startWithName}");
					params.put("startWithName", startWithName);
				});

		Optional.ofNullable(roadmapChapterFilter.getPage())
				.ifPresent(page -> {
					uriComponentsBuilder.queryParam("page", "{page}");
					params.put("page", page);
				});

		Optional.ofNullable(roadmapChapterFilter.getSize())
				.ifPresent(size -> {
					uriComponentsBuilder.queryParam("size", "{size}");
					params.put("size", size);
				});
	}
}
