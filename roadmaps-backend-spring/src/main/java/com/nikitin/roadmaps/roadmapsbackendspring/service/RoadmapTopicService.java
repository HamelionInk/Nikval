package com.nikitin.roadmaps.roadmapsbackendspring.service;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface RoadmapTopicService {

    RoadmapTopicResponseDto create(@NonNull RoadmapTopicRequestDto roadmapTopicRequestDto);

    RoadmapTopicResponseDto patch(@NonNull Long id, @NonNull RoadmapTopicRequestDto roadmapTopicRequestDto);

    RoadmapTopic getEntityById(@NonNull Long id);

    RoadmapTopicResponseDto getResponseById(@NonNull Long id);

    Page<RoadmapTopicResponseDto> getAllResponse(@NonNull RoadmapTopicFilter roadmapTopicFilter, @NonNull Pageable pageable);

    List<RoadmapTopic> getAllEntity(@NonNull RoadmapTopicFilter roadmapTopicFilter, Sort sort);

    void deleteById(@NonNull Long id);

}
