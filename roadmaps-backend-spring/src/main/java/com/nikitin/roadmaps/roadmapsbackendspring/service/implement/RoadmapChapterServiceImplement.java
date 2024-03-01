package com.nikitin.roadmaps.roadmapsbackendspring.service.implement;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapChapterFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.specification.RoadmapChapterSpecification;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter;
import com.nikitin.roadmaps.roadmapsbackendspring.exception.NotFoundException;
import com.nikitin.roadmaps.roadmapsbackendspring.mapper.RoadmapChapterMapper;
import com.nikitin.roadmaps.roadmapsbackendspring.repository.RoadmapChapterRepository;
import com.nikitin.roadmaps.roadmapsbackendspring.service.PositionEntityService;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapChapterService;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadmapChapterServiceImplement implements RoadmapChapterService, PositionEntityService<RoadmapChapter> {

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

		positionDefinition(roadmapChapter);

		return roadmapChapterMapper.toResponseDto(roadmapChapterRepository.save(roadmapChapter));
	}

	@Transactional
	@Override
	public RoadmapChapterResponseDto patch(@NonNull Long id, @NonNull RoadmapChapterRequestDto roadmapChapterRequestDto) {
		Optional.ofNullable(roadmapChapterRequestDto.getRoadmapId())
				.ifPresent(this::checkRoadmapForAvailability);

		var roadmapChapterForUpdate = getEntityById(id);

		var roadmapChapter = roadmapChapterMapper.toPatchEntity(roadmapChapterRequestDto, roadmapChapterForUpdate);

		recalculatePositions(roadmapChapter);

		return roadmapChapterMapper.toResponseDto(roadmapChapter);
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
	public Page<RoadmapChapterResponseDto> getAll(@NonNull RoadmapChapterFilter roadmapChapterFilter,
												  @NonNull Pageable pageable) {

		return roadmapChapterRepository.findAll(RoadmapChapterSpecification.filterBy(roadmapChapterFilter), pageable)
				.map(roadmapChapterMapper::toResponseDto);
	}

	@Transactional(readOnly = true)
	@Override
	public List<RoadmapChapter> getAllEntity(@NonNull RoadmapChapterFilter roadmapChapterFilter, Sort sort) {
		return roadmapChapterRepository.findAll(RoadmapChapterSpecification.filterBy(roadmapChapterFilter), sort);
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

	@Transactional
	@Override
	public void positionDefinition(RoadmapChapter roadmapChapter) {
		Optional.ofNullable(roadmapChapter.getPosition()).ifPresentOrElse(
				roadmapChapter::setPosition,
				() -> {
					var roadmapChapters = new LinkedList<>(getAllEntity(RoadmapChapterFilter.builder()
							.roadmapIds(List.of(roadmapChapter.getRoadmap().getId()))
							.build(), Sort.by(Sort.Direction.ASC, "position")));

					if (!CollectionUtils.isEmpty(roadmapChapters)) {
						roadmapChapter.setPosition(roadmapChapters.getLast().getPosition() + 1);
					} else {
						roadmapChapter.setPosition(1L);
					}
				});
	}

	@Transactional
	@Override
	public void recalculatePositions(RoadmapChapter roadmapChapter) {
		var roadmapChapters = new LinkedList<>(getAllEntity(RoadmapChapterFilter.builder()
				.roadmapIds(List.of(roadmapChapter.getRoadmap().getId()))
				.build(), Sort.by(Sort.Direction.ASC, "position")));

		for (int count = 0; count < roadmapChapters.size(); count++) {
			roadmapChapters.get(count).setPosition((long) count);
		}

		roadmapChapterRepository.saveAll(roadmapChapters);
	}

	private void checkRoadmapForAvailability(Long roadmapId) {
		roadmapService.getEntityById(roadmapId);
	}
}
