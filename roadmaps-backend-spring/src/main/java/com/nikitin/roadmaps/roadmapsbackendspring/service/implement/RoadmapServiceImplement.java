package com.nikitin.roadmaps.roadmapsbackendspring.service.implement;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapChapterFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapQuestionFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.specification.RoadmapSpecification;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Roadmap;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic;
import com.nikitin.roadmaps.roadmapsbackendspring.exception.NotFoundException;
import com.nikitin.roadmaps.roadmapsbackendspring.mapper.RoadmapMapper;
import com.nikitin.roadmaps.roadmapsbackendspring.repository.RoadmapRepository;
import com.nikitin.roadmaps.roadmapsbackendspring.service.ProfileService;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapChapterService;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapExportService;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapQuestionService;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapService;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapTopicService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadmapServiceImplement implements RoadmapService {

	private static final String ROADMAP_WITH_ID_NOT_FOUND = "Карта развития с указанным идентификатором не найдена - %s";

	private final RoadmapMapper roadmapMapper;
	private final RoadmapRepository roadmapRepository;
	private final ProfileService profileService;

	@Transactional
	@Override
	public RoadmapResponseDto create(@NonNull RoadmapRequestDto roadmapRequestDto) {
		Optional.ofNullable(roadmapRequestDto.getProfileId())
				.ifPresent(this::checkProfileForAvailability);

		var roadmap = roadmapMapper.toEntity(roadmapRequestDto);

		return roadmapMapper.toResponseDto(roadmapRepository.save(roadmap));
	}

	@Transactional
	@Override
	public RoadmapResponseDto patch(@NonNull Long id, @NonNull RoadmapRequestDto roadmapRequestDto) {
		Optional.ofNullable(roadmapRequestDto.getProfileId())
				.ifPresent(this::checkProfileForAvailability);

		var roadmapForUpdate = getEntityById(id);

		var roadmap = roadmapMapper.toPatchEntity(roadmapRequestDto, roadmapForUpdate);

		return roadmapMapper.toResponseDto(roadmapRepository.save(roadmap));
	}

	@Transactional(readOnly = true)
	@Override
	public Roadmap getEntityById(@NonNull Long id) {
		return roadmapRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format(ROADMAP_WITH_ID_NOT_FOUND, id)));
	}

	@Transactional(readOnly = true)
	@Override
	public RoadmapResponseDto getResponseById(@NonNull Long id) {
		return roadmapMapper.toResponseDto(getEntityById(id));
	}

	@Transactional(readOnly = true)
	@Override
	public Page<RoadmapResponseDto> getAll(@NonNull RoadmapFilter roadmapFilter, @NonNull Pageable pageable) {
		return roadmapRepository.findAll(RoadmapSpecification.filterBy(roadmapFilter), pageable)
				.map(roadmapMapper::toResponseDto);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<RoadmapResponseDto> getAllByProfileId(@NonNull Long id, @NonNull Pageable pageable) {
		return roadmapRepository.findAllByProfileId(id, pageable)
				.map(roadmapMapper::toResponseDto);
	}

	@Transactional
	@Override
	public void deleteById(@NonNull Long id) {
		var roadmap = getEntityById(id);

		roadmapRepository.delete(roadmap);
	}

	private void checkProfileForAvailability(Long profileId) {
		profileService.getEntityById(profileId);
	}
}
