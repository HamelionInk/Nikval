package com.nikitin.roadmaps.roadmapsbackendspring.service;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Roadmap;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoadmapService {

    RoadmapResponseDto create(@NonNull RoadmapRequestDto roadmapRequestDto);

    RoadmapResponseDto patch(@NonNull Long id, @NonNull RoadmapRequestDto roadmapRequestDto);

    Roadmap getEntityById(@NonNull Long id);

    RoadmapResponseDto getResponseById(@NonNull Long id);

    Page<RoadmapResponseDto> getAll(@NonNull Pageable pageable);

    Page<RoadmapResponseDto> getAllByProfileId(@NonNull Long id, @NonNull Pageable pageable);
}
