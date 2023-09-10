package com.nikitin.roadmaps.roadmapsbackendspring.controller;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapService;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Create;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Patch;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
@RequestMapping("/roadmaps")
@Tag(name = "Карта развития")
public class RoadmapController {

    private final RoadmapService roadmapService;

    @PostMapping
    public ResponseEntity<RoadmapResponseDto> create(@RequestBody @Validated(value = Create.class) RoadmapRequestDto roadmapRequestDto) {
        var responseBody = roadmapService.create(roadmapRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PostMapping("/{roadmapId}/chapter")
    public ResponseEntity<RoadmapResponseDto> createChapter(@PathVariable(name = "roadmapId") Long roadmapId,
                                                                   @RequestBody @Validated(value = Create.class) RoadmapChapterRequestDto roadmapChapterRequestDto) {
        var responseBody = roadmapService.createChapter(roadmapId, roadmapChapterRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PostMapping("/chapter/{chapterId}/topic")
    public ResponseEntity<RoadmapChapterResponseDto> createTopic(@PathVariable(name = "chapterId") Long chapterId,
                                                               @RequestBody @Validated(value = Create.class) RoadmapTopicRequestDto roadmapTopicRequestDto) {
        var responseBody = roadmapService.createTopic(chapterId, roadmapTopicRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PostMapping("/topic/{topicId}/question")
    public ResponseEntity<RoadmapTopicResponseDto> createQuestion(@PathVariable(name = "topicId") Long topicId,
                                                                  @RequestBody @Validated(value = Create.class) RoadmapQuestionRequestDto roadmapQuestionRequestDto) {
        var responseBody = roadmapService.createQuestion(topicId, roadmapQuestionRequestDto);

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

    @GetMapping
    public ResponseEntity<Page<RoadmapResponseDto>> getAll(@ParameterObject @PageableDefault(sort = "name",
            direction = Sort.Direction.ASC,
            size = Integer.MAX_VALUE) Pageable pageable) {
        var responseBody = roadmapService.getAll(pageable);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<Page<RoadmapResponseDto>> getAllByProfileId(@PathVariable(name = "id") Long id,
                                                                      @ParameterObject @PageableDefault(sort = "name",
                                                                              direction = Sort.Direction.ASC,
                                                                              size = Integer.MAX_VALUE) Pageable pageable) {
        var responseBody = roadmapService.getAllByProfileId(id, pageable);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @GetMapping("/topic/{chapterId}")
    public ResponseEntity<Page<RoadmapTopicResponseDto>> getAllTopicByChapterId(@PathVariable(name = "chapterId") Long chapterId,
                                                                         @ParameterObject @PageableDefault(sort = "name",
                                                                                 direction = Sort.Direction.ASC,
                                                                                 size = Integer.MAX_VALUE) Pageable pageable) {
        var responseBody = roadmapService.getAllTopicByChapterId(chapterId, pageable);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @GetMapping("/question/{topicId}")
    public ResponseEntity<Page<RoadmapQuestionResponseDto>> getAllQuestionByTopicId(@PathVariable(name = "topicId") Long topicId,
                                                                                    @ParameterObject @PageableDefault(sort = "question",
                                                                                            direction = Sort.Direction.ASC,
                                                                                            size = Integer.MAX_VALUE) Pageable pageable) {
        var responseBody = roadmapService.getAllQuestionByTopicId(topicId, pageable);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") Long id) {
        roadmapService.deleteRoadmapById(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/chapter/{chapterId}")
    public ResponseEntity<?> deleteChapterById(@PathVariable(name = "chapterId") Long chapterId) {
        roadmapService.deleteChapterById(chapterId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/topic/{topicId}")
    public ResponseEntity<?> deleteTopicById(@PathVariable(name = "topicId") Long topicId) {
        roadmapService.deleteTopicById(topicId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/question/{questionId}")
    public ResponseEntity<?> deleteQuestionById(@PathVariable(name = "questionId") Long questionId) {
        roadmapService.deleteQuestionById(questionId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
