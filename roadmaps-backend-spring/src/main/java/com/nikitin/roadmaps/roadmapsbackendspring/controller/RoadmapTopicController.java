package com.nikitin.roadmaps.roadmapsbackendspring.controller;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapTopicService;
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
@RequestMapping("/roadmap-topics")
@Tag(name = "Тема раздела")
public class RoadmapTopicController {

    private final RoadmapTopicService roadmapTopicService;

    @PostMapping
    public ResponseEntity<RoadmapTopicResponseDto> create(@RequestBody @Validated(value = Create.class) RoadmapTopicRequestDto roadmapTopicRequestDto) {
        var responseBody = roadmapTopicService.create(roadmapTopicRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoadmapTopicResponseDto> patch(@PathVariable(name = "id") Long id,
                                                         @RequestBody @Validated(value = Patch.class) RoadmapTopicRequestDto roadmapTopicRequestDto) {
        var responseBody = roadmapTopicService.patch(id, roadmapTopicRequestDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoadmapTopicResponseDto> getById(@PathVariable(name = "id") Long id) {
        var responseBody = roadmapTopicService.getResponseById(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @PageableAsQueryParam
    @GetMapping
    public ResponseEntity<Page<RoadmapTopicResponseDto>> getAll(@ModelAttribute RoadmapTopicFilter roadmapTopicFilter,
                                                                @ParameterObject @PageableDefault(sort = "position",
                                                                        direction = Sort.Direction.ASC,
                                                                        size = Integer.MAX_VALUE) Pageable pageable) {
        var responseBody = roadmapTopicService.getAllResponse(roadmapTopicFilter, pageable);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") Long id) {
        roadmapTopicService.deleteById(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


}
