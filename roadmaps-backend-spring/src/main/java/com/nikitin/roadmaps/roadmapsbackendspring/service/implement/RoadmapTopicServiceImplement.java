package com.nikitin.roadmaps.roadmapsbackendspring.service.implement;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.specification.RoadmapTopicSpecification;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic;
import com.nikitin.roadmaps.roadmapsbackendspring.exception.NotFoundException;
import com.nikitin.roadmaps.roadmapsbackendspring.mapper.RoadmapTopicMapper;
import com.nikitin.roadmaps.roadmapsbackendspring.repository.RoadmapTopicRepository;
import com.nikitin.roadmaps.roadmapsbackendspring.service.PositionEntityService;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapChapterService;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapTopicService;
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
public class RoadmapTopicServiceImplement implements RoadmapTopicService, PositionEntityService<RoadmapTopic> {

    private static final String TOPIC_WITH_ID_NOT_FOUND = "Тема с указанным идентификатором не найдена - %s";

    private final RoadmapTopicMapper roadmapTopicMapper;
    private final RoadmapTopicRepository roadmapTopicRepository;
    private final RoadmapChapterService roadmapChapterService;

    @Transactional
    @Override
    public RoadmapTopicResponseDto create(@NonNull RoadmapTopicRequestDto roadmapTopicRequestDto) {
        Optional.ofNullable(roadmapTopicRequestDto.getRoadmapChapterId())
                .ifPresent(this::checkRoadmapChapterForAvailability);

        var roadmapTopic = roadmapTopicMapper.toEntity(roadmapTopicRequestDto);

        positionDefinition(roadmapTopic);

        return roadmapTopicMapper.toResponseDto(roadmapTopicRepository.save(roadmapTopic));
    }

    @Transactional
    @Override
    public RoadmapTopicResponseDto patch(@NonNull Long id, @NonNull RoadmapTopicRequestDto roadmapTopicRequestDto) {
        Optional.ofNullable(roadmapTopicRequestDto.getRoadmapChapterId())
                .ifPresent(this::checkRoadmapChapterForAvailability);

        var roadmapTopicForUpdate = getEntityById(id);

        var roadmapTopic = roadmapTopicMapper.toPatchEntity(roadmapTopicRequestDto, roadmapTopicForUpdate);

        recalculatePositions(roadmapTopic);

        return roadmapTopicMapper.toResponseDto(roadmapTopic);
    }

    @Transactional(readOnly = true)
    @Override
    public RoadmapTopic getEntityById(@NonNull Long id) {
        return roadmapTopicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(TOPIC_WITH_ID_NOT_FOUND, id)));
    }

    @Transactional(readOnly = true)
    @Override
    public RoadmapTopicResponseDto getResponseById(@NonNull Long id) {
        return roadmapTopicMapper.toResponseDto(getEntityById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RoadmapTopicResponseDto> getAll(@NonNull RoadmapTopicFilter roadmapTopicFilter, @NonNull Pageable pageable) {
        return roadmapTopicRepository.findAll(RoadmapTopicSpecification.filterBy(roadmapTopicFilter), pageable)
                .map(roadmapTopicMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoadmapTopic> getAllEntity(@NonNull RoadmapTopicFilter roadmapTopicFilter, Sort sort) {
        return roadmapTopicRepository.findAll(RoadmapTopicSpecification.filterBy(roadmapTopicFilter), sort);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RoadmapTopicResponseDto> getAllByChapterId(@NonNull Long id, @NonNull Pageable pageable) {
        return roadmapTopicRepository.findAllByRoadmapChapterId(id, pageable)
                .map(roadmapTopicMapper::toResponseDto);
    }

    @Transactional
    @Override
    public void deleteById(@NonNull Long id) {
        var roadmapTopic = getEntityById(id);

        roadmapTopicRepository.delete(roadmapTopic);
    }

    @Override
    public void updateNumberOfQuestion(@NonNull Long id, @NonNull Integer numberOfQuestions) {
        var roadmapTopic = getEntityById(id);
        roadmapTopic.setNumberOfQuestion(numberOfQuestions);

        roadmapTopicRepository.save(roadmapTopic);
    }

    @Override
    public void updateNumberExploredQuestion(@NonNull Long id, @NonNull Long numberOfExploredQuestions) {
        var roadmapTopic = getEntityById(id);
        roadmapTopic.setNumberExploredQuestion(numberOfExploredQuestions.intValue());

        roadmapTopicRepository.save(roadmapTopic);
    }

    @Transactional
    @Override
    public void positionDefinition(RoadmapTopic roadmapTopic) {
        Optional.ofNullable(roadmapTopic.getPosition()).ifPresentOrElse(
                roadmapTopic::setPosition,
                () -> {
                    var roadmapTopics = new LinkedList<>(getAllEntity(RoadmapTopicFilter.builder()
                            .roadmapChapterIds(List.of(roadmapTopic.getRoadmapChapter().getId()))
                            .build(), Sort.by(Sort.Direction.ASC, "position")));

                    if (!CollectionUtils.isEmpty(roadmapTopics)) {
                        roadmapTopic.setPosition(roadmapTopics.getLast().getPosition() + 1);
                    } else {
                        roadmapTopic.setPosition(1L);
                    }
                });
    }

    @Transactional
    @Override
    public void recalculatePositions(RoadmapTopic roadmapTopic) {
        var roadmapTopics = new LinkedList<>(getAllEntity(RoadmapTopicFilter.builder()
                .roadmapChapterIds(List.of(roadmapTopic.getRoadmapChapter().getId()))
                .build(), Sort.by(Sort.Direction.ASC, "position")));

        for (int count = 0; count < roadmapTopics.size(); count++) {
            roadmapTopics.get(count).setPosition((long) count);
        }

        roadmapTopicRepository.saveAll(roadmapTopics);
    }

    private void checkRoadmapChapterForAvailability(Long roadmapChapterId) {
        roadmapChapterService.getEntityById(roadmapChapterId);
    }
}
