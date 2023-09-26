package com.nikitin.roadmaps.roadmapsbackendspring.service;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapQuestionFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapQuestion;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoadmapQuestionService {

    RoadmapQuestionResponseDto create(@NonNull RoadmapQuestionRequestDto roadmapQuestionRequestDto);

    RoadmapQuestionResponseDto patch(@NonNull Long id, @NonNull RoadmapQuestionRequestDto roadmapQuestionRequestDto);

    RoadmapQuestion getEntityById(@NonNull Long id);

    RoadmapQuestionResponseDto getResponseById(@NonNull Long id);

    Page<RoadmapQuestionResponseDto> getAll(@NonNull RoadmapQuestionFilter roadmapQuestionFilter, @NonNull Pageable pageable);

    Page<RoadmapQuestionResponseDto> getAllByTopicId(@NonNull Long id, @NonNull Pageable pageable);

    void deleteById(@NonNull Long id);
}
