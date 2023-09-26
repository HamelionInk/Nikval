package com.nikitin.roadmaps.roadmapsbackendspring.controller;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapService;
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
@RequestMapping("/roadmaps")
@Tag(name = "Карта развития")
public class RoadmapController {

    private final RoadmapService roadmapService;

    @PostMapping
    public ResponseEntity<RoadmapResponseDto> create(@RequestBody @Validated(value = Create.class) RoadmapRequestDto roadmapRequestDto) {
        var responseBody = roadmapService.create(roadmapRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoadmapResponseDto> patch(@PathVariable(name = "id") Long id,
                                                    @RequestBody @Validated(value = Patch.class) RoadmapRequestDto roadmapRequestDto) {
        var responseBody = roadmapService.patch(id, roadmapRequestDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoadmapResponseDto> getById(@PathVariable(name = "id") Long id) {
        var responseBody = roadmapService.getResponseById(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @PageableAsQueryParam
    @GetMapping
    public ResponseEntity<Page<RoadmapResponseDto>> getAll(@ModelAttribute RoadmapFilter roadmapFilter,
                                                           @ParameterObject @PageableDefault(sort = "id",
                                                                   direction = Sort.Direction.ASC,
                                                                   size = Integer.MAX_VALUE) Pageable pageable) {
        var responseBody = roadmapService.getAll(roadmapFilter, pageable);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @PageableAsQueryParam
    @GetMapping("/profile/{id}")
    public ResponseEntity<Page<RoadmapResponseDto>> getAllByProfileId(@PathVariable(name = "id") Long id, @ParameterObject @PageableDefault(
            sort = "id", direction = Sort.Direction.ASC, size = Integer.MAX_VALUE) Pageable pageable) {
        var responseBody = roadmapService.getAllByProfileId(id, pageable);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") Long id) {
        roadmapService.deleteById(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
