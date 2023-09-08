package com.nikitin.roadmaps.roadmapsbackendspring.service.implement;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Roadmap;
import com.nikitin.roadmaps.roadmapsbackendspring.exception.NotFoundException;
import com.nikitin.roadmaps.roadmapsbackendspring.mapper.RoadmapMapper;
import com.nikitin.roadmaps.roadmapsbackendspring.repository.RoadmapRepository;
import com.nikitin.roadmaps.roadmapsbackendspring.service.ProfileService;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadmapServiceImplement implements RoadmapService {

    private static final String PROFILE_WITH_ID_NOT_FOUND = "Профиль с указанным идентификатором не найден - %s";
    private static final String ROADMAP_WITH_ID_NOT_FOUND = "Карта развития с указанным идентификатором не найдена - %s";

    private final ProfileService profileService;
    private final RoadmapRepository roadmapRepository;
    private final RoadmapMapper roadmapMapper;

    @Override
    public RoadmapResponseDto create(@NonNull RoadmapRequestDto roadmapRequestDto) {
        Optional.ofNullable(profileService.getEntityById(roadmapRequestDto.getProfileId()))
                .orElseThrow(() -> new NotFoundException(String.format(PROFILE_WITH_ID_NOT_FOUND, roadmapRequestDto.getProfileId())));

        var roadmap = roadmapMapper.toEntity(roadmapRequestDto);

        return roadmapMapper.toResponseDto(roadmapRepository.save(roadmap));
    }

    @Override
    public RoadmapResponseDto patch(@NonNull Long id, @NonNull RoadmapRequestDto roadmapRequestDto) {
        var roadmapForUpdate = getEntityById(id);

        var roadmap = roadmapMapper.toPatchEntity(roadmapRequestDto, roadmapForUpdate);
        return roadmapMapper.toResponseDto(roadmapRepository.save(roadmap));
    }

    @Override
    public Roadmap getEntityById(@NonNull Long id) {
        return roadmapRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ROADMAP_WITH_ID_NOT_FOUND, id)));
    }

    @Override
    public RoadmapResponseDto getResponseById(@NonNull Long id) {
        return roadmapMapper.toResponseDto(getEntityById(id));
    }

    @Override
    public Page<RoadmapResponseDto> getAll(@NonNull Pageable pageable) {
        return roadmapRepository.findAll(pageable)
                .map(roadmapMapper::toResponseDto);
    }

    @Override
    public Page<RoadmapResponseDto> getAllByProfileId(@NonNull Long id, @NonNull Pageable pageable) {
        return roadmapRepository.findAllByProfileId(id, pageable)
                .map(roadmapMapper::toResponseDto);
    }
}
