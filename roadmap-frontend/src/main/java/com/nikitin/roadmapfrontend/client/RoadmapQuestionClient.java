package com.nikitin.roadmapfrontend.client;

import com.nikitin.roadmapfrontend.configuration.rest.RestTemplateService;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapFilter;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapQuestionFilter;
import com.nikitin.roadmapfrontend.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmapfrontend.dto.request.RoadmapRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapResponseDto;
import com.nikitin.roadmapfrontend.dto.response.pageable.PageableRoadmapQuestionResponseDto;
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
public class RoadmapQuestionClient {

	private final RestTemplateService restTemplateService;

	public RoadmapQuestionResponseDto create(RoadmapQuestionRequestDto roadmapQuestionRequestDto) {
		return restTemplateService.request(
				"/roadmap-questions",
				HttpMethod.POST,
				restTemplateService.buildRequestBody(roadmapQuestionRequestDto, null),
				Collections.EMPTY_MAP,
				RoadmapQuestionResponseDto.class
		);
	}

	public RoadmapQuestionResponseDto patch(Long id, RoadmapQuestionRequestDto roadmapQuestionRequestDto) {
		return restTemplateService.request(
				"/roadmap-questions/{id}",
				HttpMethod.PATCH,
				restTemplateService.buildRequestBody(roadmapQuestionRequestDto, null),
				Collections.singletonMap("id", id),
				RoadmapQuestionResponseDto.class
		);
	}

	public PageableRoadmapQuestionResponseDto getAll(RoadmapQuestionFilter roadmapFilter) {
		var url = UriComponentsBuilder.fromUriString("/roadmap-questions");
		var params = new HashMap<String, Object>();

		generateUriParams(params, url, roadmapFilter);

		return restTemplateService.request(
				url.encode().toUriString(),
				HttpMethod.GET,
				restTemplateService.buildRequestBody(null, null),
				params,
				PageableRoadmapQuestionResponseDto.class
		);
	}

	public void deleteById(Long id) {
		restTemplateService.request(
				"/roadmap-questions/{id}",
				HttpMethod.DELETE,
				restTemplateService.buildRequestBody(null, null),
				Collections.singletonMap("id", id),
				Void.class
		);
	}

	private void generateUriParams(Map<String, Object> params, UriComponentsBuilder uriComponentsBuilder, RoadmapQuestionFilter roadmapFilter) {
		Optional.ofNullable(roadmapFilter.getRoadmapTopicIds())
				.ifPresent(roadmapTopicIds -> roadmapTopicIds.forEach(item -> {
					uriComponentsBuilder.queryParam("roadmapTopicIds", "{roadmapTopicIds}");
					params.put("roadmapTopicIds", item);
				}));

		Optional.ofNullable(roadmapFilter.getIds())
				.ifPresent(ids -> ids.forEach(item -> {
					uriComponentsBuilder.queryParam("ids", "{ids}");
					params.put("ids", item);
				}));

		Optional.ofNullable(roadmapFilter.getStartWithQuestion())
				.ifPresent(startWithQuestion -> {
					uriComponentsBuilder.queryParam("startWithQuestion", "{startWithQuestion}");
					params.put("startWithQuestion", startWithQuestion);
				});

		Optional.ofNullable(roadmapFilter.getStartWithAnswer())
				.ifPresent(startWithAnswer -> {
					uriComponentsBuilder.queryParam("startWithAnswer", "{startWithAnswer}");
					params.put("startWithAnswer", startWithAnswer);
				});

		Optional.ofNullable(roadmapFilter.getIsExplored())
				.ifPresent(isExplored -> {
					uriComponentsBuilder.queryParam("isExplored", "{isExplored}");
					params.put("isExplored", isExplored);
				});
	}
}
