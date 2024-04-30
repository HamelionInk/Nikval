package com.nikitin.roadmaps.roadmapsbackendspring.controller;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapChapterFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapChapterService;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Create;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Patch;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/roadmap-chapters")
@Tag(name = "Раздел карты развития")
public class RoadmapChapterController {

	private final RoadmapChapterService roadmapChapterService;

	@PostMapping
	public ResponseEntity<RoadmapChapterResponseDto> create(@RequestBody @Validated(value = Create.class) RoadmapChapterRequestDto roadmapChapterRequestDto) {
		var responseBody = roadmapChapterService.create(roadmapChapterRequestDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<RoadmapChapterResponseDto> patch(@PathVariable(name = "id") Long id,
														   @RequestBody @Validated(value = Patch.class) RoadmapChapterRequestDto roadmapChapterRequestDto) {
		var responseBody = roadmapChapterService.patch(id, roadmapChapterRequestDto);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
	}

	@GetMapping("/{id}")
	public ResponseEntity<RoadmapChapterResponseDto> getById(@PathVariable(name = "id") Long id) {
		var responseBody = roadmapChapterService.getResponseById(id);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
	}

	@PageableAsQueryParam
	@GetMapping
	public ResponseEntity<Page<RoadmapChapterResponseDto>> getAll(@ModelAttribute RoadmapChapterFilter roadmapChapterFilter,
																  @ParameterObject @PageableDefault(sort = "position",
																		  direction = Sort.Direction.ASC,
																		  size = Integer.MAX_VALUE) Pageable pageable) {
		var responseBody = roadmapChapterService.getAll(roadmapChapterFilter, pageable);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
	}

	@PageableAsQueryParam
	@GetMapping("/roadmap/{id}")
	public ResponseEntity<Page<RoadmapChapterResponseDto>> getAllByRoadmapId(@PathVariable(name = "id") Long id, @ParameterObject @PageableDefault(
			sort = "id", direction = Sort.Direction.ASC, size = Integer.MAX_VALUE) Pageable pageable) {
		var responseBody = roadmapChapterService.getAllByRoadmapId(id, pageable);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable(name = "id") Long id) {
		roadmapChapterService.deleteById(id);

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
}
