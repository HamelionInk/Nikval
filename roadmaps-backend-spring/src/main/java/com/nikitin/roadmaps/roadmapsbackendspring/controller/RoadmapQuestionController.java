package com.nikitin.roadmaps.roadmapsbackendspring.controller;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapQuestionService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/roadmap-questions")
@Tag(name = "Вопросы по теме")
public class RoadmapQuestionController {

    private final RoadmapQuestionService roadmapQuestionService;

    @PostMapping
    public ResponseEntity<RoadmapQuestionResponseDto> create(@RequestBody @Validated(value = Create.class) RoadmapQuestionRequestDto roadmapQuestionRequestDto) {
        var responseBody = roadmapQuestionService.create(roadmapQuestionRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoadmapQuestionResponseDto> patch(@PathVariable(name = "id") Long id,
                                                            @RequestBody @Validated(value = Patch.class) RoadmapQuestionRequestDto roadmapQuestionRequestDto) {
        var responseBody = roadmapQuestionService.patch(id, roadmapQuestionRequestDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoadmapQuestionResponseDto> getById(@PathVariable(name = "id") Long id) {
        var responseBody = roadmapQuestionService.getResponseById(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @PageableAsQueryParam
    @GetMapping
    public ResponseEntity<Page<RoadmapQuestionResponseDto>> getAll(@ParameterObject @PageableDefault(sort = "id",
            direction = Sort.Direction.ASC, size = Integer.MAX_VALUE) Pageable pageable) {
        var responseBody = roadmapQuestionService.getAll(pageable);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @PageableAsQueryParam
    @GetMapping("/roadmap-topic/{id}")
    public ResponseEntity<Page<RoadmapQuestionResponseDto>> getAllByTopicId(@PathVariable(name = "id") Long id, @ParameterObject @PageableDefault(
            sort = "id", direction = Sort.Direction.ASC, size = Integer.MAX_VALUE) Pageable pageable) {
        var responseBody = roadmapQuestionService.getAllByTopicId(id, pageable);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") Long id) {
        roadmapQuestionService.deleteById(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
