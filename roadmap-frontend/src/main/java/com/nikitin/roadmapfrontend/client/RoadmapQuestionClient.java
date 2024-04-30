package com.nikitin.roadmapfrontend.client;

import com.nikitin.roadmapfrontend.configuration.rest.RestTemplateService;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapQuestionFilter;
import com.nikitin.roadmapfrontend.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmapfrontend.dto.response.pageable.PageableRoadmapQuestionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
				null,
				RoadmapQuestionResponseDto.class
		);
	}

	public RoadmapQuestionResponseDto patch(Long id, RoadmapQuestionRequestDto roadmapQuestionRequestDto) {
		return restTemplateService.request(
				"/roadmap-questions/{id}",
				HttpMethod.PATCH,
				restTemplateService.buildRequestBody(roadmapQuestionRequestDto, null),
				Map.ofEntries(Map.entry("id", id)),
				RoadmapQuestionResponseDto.class
		);
	}

	public PageableRoadmapQuestionResponseDto getAll(RoadmapQuestionFilter roadmapFilter) {
		var uriParams = new HashMap<String, Object>();
		uriParams.put("roadmapTopicIds", roadmapFilter.getRoadmapTopicIds());
		uriParams.put("ids", roadmapFilter.getIds());
		uriParams.put("startWithQuestion", roadmapFilter.getStartWithQuestion());
		uriParams.put("startWithAnswer", roadmapFilter.getStartWithAnswer());
		uriParams.put("exploredStatuses", roadmapFilter.getExploredStatuses());

		return restTemplateService.request(
				"/roadmap-questions",
				HttpMethod.GET,
				restTemplateService.buildRequestBody(null, null),
				uriParams,
				PageableRoadmapQuestionResponseDto.class
		);
	}

	public void deleteById(Long id) {
		restTemplateService.request(
				"/roadmap-questions/{id}",
				HttpMethod.DELETE,
				restTemplateService.buildRequestBody(null, null),
				Map.ofEntries(Map.entry("id", id)),
				Void.class
		);
	}
}
