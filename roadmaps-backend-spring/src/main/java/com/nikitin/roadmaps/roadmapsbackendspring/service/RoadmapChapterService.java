package com.nikitin.roadmaps.roadmapsbackendspring.service;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapChapterFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface RoadmapChapterService {

	RoadmapChapterResponseDto create(@NonNull RoadmapChapterRequestDto roadmapChapterRequestDto);

	RoadmapChapterResponseDto patch(@NonNull Long id, @NonNull RoadmapChapterRequestDto roadmapChapterRequestDto);

	RoadmapChapter getEntityById(@NonNull Long id);

	RoadmapChapterResponseDto getResponseById(@NonNull Long id);

	Page<RoadmapChapterResponseDto> getAll(@NonNull RoadmapChapterFilter roadmapChapterFilter,
										   @NonNull Pageable pageable);

	List<RoadmapChapter> getAllEntity(@NonNull RoadmapChapterFilter roadmapChapterFilter, Sort sort);

	Page<RoadmapChapterResponseDto> getAllByRoadmapId(@NonNull Long id, @NonNull Pageable pageable);

	void deleteById(@NonNull Long id);
}
