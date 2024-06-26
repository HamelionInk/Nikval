package com.nikitin.roadmaps.roadmapsbackendspring.service;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapQuestionFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapQuestion;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface RoadmapQuestionService {

    RoadmapQuestionResponseDto create(@NonNull RoadmapQuestionRequestDto roadmapQuestionRequestDto);

    RoadmapQuestionResponseDto patch(@NonNull Long id, @NonNull RoadmapQuestionRequestDto roadmapQuestionRequestDto);

    RoadmapQuestion getEntityById(@NonNull Long id);

    RoadmapQuestionResponseDto getResponseById(@NonNull Long id);

    Page<RoadmapQuestionResponseDto> getAllResponse(@NonNull RoadmapQuestionFilter roadmapQuestionFilter, @NonNull Pageable pageable);

    List<RoadmapQuestion> getAllEntity(@NonNull RoadmapQuestionFilter roadmapQuestionFilter, Sort sort);

    void deleteById(@NonNull Long id);
}
