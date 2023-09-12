package com.nikitin.roadmaps.roadmapsbackendspring.service.implement;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter;
import com.nikitin.roadmaps.roadmapsbackendspring.exception.NotFoundException;
import com.nikitin.roadmaps.roadmapsbackendspring.mapper.RoadmapChapterMapper;
import com.nikitin.roadmaps.roadmapsbackendspring.repository.RoadmapChapterRepository;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapChapterService;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadmapChapterServiceImplement implements RoadmapChapterService {

    private static final String CHAPTER_WITH_ID_NOT_FOUND = "Раздел с указанным идентификатором не найден - %s";

    private final RoadmapChapterMapper roadmapChapterMapper;
    private final RoadmapChapterRepository roadmapChapterRepository;
    private final RoadmapService roadmapService;

    @Transactional
    @Override
    public RoadmapChapterResponseDto create(@NonNull RoadmapChapterRequestDto roadmapChapterRequestDto) {
        Optional.ofNullable(roadmapChapterRequestDto.getRoadmapId())
                .ifPresent(this::checkRoadmapForAvailability);

        var roadmapChapter = roadmapChapterMapper.toEntity(roadmapChapterRequestDto);

        return roadmapChapterMapper.toResponseDto(roadmapChapterRepository.save(roadmapChapter));
    }

    @Transactional
    @Override
    public RoadmapChapterResponseDto patch(@NonNull Long id, @NonNull RoadmapChapterRequestDto roadmapChapterRequestDto) {
        Optional.ofNullable(roadmapChapterRequestDto.getRoadmapId())
                .ifPresent(this::checkRoadmapForAvailability);

        var roadmapChapterForUpdate = getEntityById(id);

        var roadmapChapter = roadmapChapterMapper.toPatchEntity(roadmapChapterRequestDto, roadmapChapterForUpdate);

        return roadmapChapterMapper.toResponseDto(roadmapChapterRepository.save(roadmapChapter));
    }

    @Transactional(readOnly = true)
    @Override
    public RoadmapChapter getEntityById(@NonNull Long id) {
        return roadmapChapterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(CHAPTER_WITH_ID_NOT_FOUND, id)));
    }

    @Transactional(readOnly = true)
    @Override
    public RoadmapChapterResponseDto getResponseById(@NonNull Long id) {
        return roadmapChapterMapper.toResponseDto(getEntityById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RoadmapChapterResponseDto> getAll(@NonNull Pageable pageable) {
        return roadmapChapterRepository.findAll(pageable)
                .map(roadmapChapterMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RoadmapChapterResponseDto> getAllByRoadmapId(@NonNull Long id, @NonNull Pageable pageable) {
        return roadmapChapterRepository.findAllByRoadmapId(id, pageable)
                .map(roadmapChapterMapper::toResponseDto);
    }

    @Transactional
    @Override
    public void deleteById(@NonNull Long id) {
        var roadmapChapter = getEntityById(id);

        roadmapChapterRepository.delete(roadmapChapter);
    }

    private void checkRoadmapForAvailability(Long roadmapId) {
        roadmapService.getEntityById(roadmapId);
    }
}
