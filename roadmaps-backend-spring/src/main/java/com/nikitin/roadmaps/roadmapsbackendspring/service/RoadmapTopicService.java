package com.nikitin.roadmaps.roadmapsbackendspring.service;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoadmapTopicService {

    RoadmapTopicResponseDto create(@NonNull RoadmapTopicRequestDto roadmapTopicRequestDto);

    RoadmapTopicResponseDto patch(@NonNull Long id, @NonNull RoadmapTopicRequestDto roadmapTopicRequestDto);

    RoadmapTopic getEntityById(@NonNull Long id);

    RoadmapTopicResponseDto getResponseById(@NonNull Long id);

    Page<RoadmapTopicResponseDto> getAll(@NonNull Pageable pageable);

    Page<RoadmapTopicResponseDto> getAllByChapterId(@NonNull Long id, @NonNull Pageable pageable);

    void deleteById(@NonNull Long id);

    void updateNumberOfQuestion(@NonNull Long id, @NonNull Integer numberOfQuestions);

    void updateNumberExploredQuestion(@NonNull Long id, @NonNull Long numberOfExploredQuestions);
}
