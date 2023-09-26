package com.nikitin.roadmaps.roadmapsbackendspring.service;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapFilter;
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

    RoadmapResponseDto patch(@NonNull Long id, @NonNull RoadmapRequestDto roadmapRequestDto);

    Roadmap getEntityById(@NonNull Long id);

    RoadmapResponseDto getResponseById(@NonNull Long id);

    Page<RoadmapResponseDto> getAll(@NonNull RoadmapFilter roadmapFilter, @NonNull Pageable pageable);

    Page<RoadmapResponseDto> getAllByProfileId(@NonNull Long id, @NonNull Pageable pageable);

    void deleteById(@NonNull Long id);
}
