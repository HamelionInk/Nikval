package com.nikitin.roadmaps.roadmapsbackendspring.service;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Roadmap;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapQuestion;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoadmapService {

    RoadmapResponseDto create(@NonNull RoadmapRequestDto roadmapRequestDto);

    RoadmapResponseDto createChapter(@NonNull Long roadmapId,@NonNull RoadmapChapterRequestDto roadmapChapterRequestDto);

    RoadmapChapterResponseDto createTopic(@NonNull Long chapterId, @NonNull RoadmapTopicRequestDto roadmapTopicRequestDto);

    RoadmapTopicResponseDto createQuestion(@NonNull Long topicId, @NonNull RoadmapQuestionRequestDto roadmapQuestionRequestDto);

    RoadmapResponseDto patch(@NonNull Long id, @NonNull RoadmapRequestDto roadmapRequestDto);

    Roadmap getEntityById(@NonNull Long id);

    RoadmapResponseDto getResponseById(@NonNull Long id);

    RoadmapChapter getEntityChapterById(@NonNull Long id);

    RoadmapChapterResponseDto getResponseChapterById(@NonNull Long id);

    RoadmapTopic getEntityTopicById(@NonNull Long id);

    RoadmapTopicResponseDto getResponseTopicById(@NonNull Long id);

    RoadmapQuestion getEntityQuestionById(@NonNull Long id);

    RoadmapQuestionResponseDto getResponseQuestionById(@NonNull Long id);

    Page<RoadmapResponseDto> getAll(@NonNull Pageable pageable);

    Page<RoadmapResponseDto> getAllByProfileId(@NonNull Long id, @NonNull Pageable pageable);

    Page<RoadmapTopicResponseDto> getAllTopicByChapterId(@NonNull Long id, @NonNull Pageable pageable);

    Page<RoadmapQuestionResponseDto> getAllQuestionByTopicId(@NonNull Long id, @NonNull Pageable pageable);

    void deleteRoadmapById(@NonNull Long id);

    void deleteChapterById(@NonNull Long id);

    void deleteTopicById(@NonNull Long id);

    void deleteQuestionById(@NonNull Long id);
}
