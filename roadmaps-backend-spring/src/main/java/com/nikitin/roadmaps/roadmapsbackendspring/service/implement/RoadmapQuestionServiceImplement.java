package com.nikitin.roadmaps.roadmapsbackendspring.service.implement;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapQuestionFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.specification.RoadmapQuestionSpecification;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapQuestion;
import com.nikitin.roadmaps.roadmapsbackendspring.exception.NotFoundException;
import com.nikitin.roadmaps.roadmapsbackendspring.mapper.RoadmapQuestionMapper;
import com.nikitin.roadmaps.roadmapsbackendspring.repository.RoadmapQuestionRepository;
import com.nikitin.roadmaps.roadmapsbackendspring.service.PositionEntityService;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapQuestionService;
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
public class RoadmapQuestionServiceImplement implements RoadmapQuestionService, PositionEntityService<RoadmapQuestion> {

    private static final String QUESTION_WITH_ID_NOT_FOUND = "Вопрос с указанным идентификатором не найден - %s";

    private final RoadmapQuestionMapper roadmapQuestionMapper;
    private final RoadmapQuestionRepository roadmapQuestionRepository;
    private final RoadmapTopicService roadmapTopicService;

    @Transactional
    @Override
    public RoadmapQuestionResponseDto create(@NonNull RoadmapQuestionRequestDto roadmapQuestionRequestDto) {
        Optional.ofNullable(roadmapQuestionRequestDto.getRoadmapTopicId())
                .ifPresent(this::checkRoadmapTopicForAvailability);

        var roadmapQuestion = roadmapQuestionMapper.toEntity(roadmapQuestionRequestDto);
        positionDefinition(roadmapQuestion);

        var roadmapQuestionAfterSave = roadmapQuestionRepository.save(roadmapQuestion);

        var roadmapTopicId = roadmapQuestionAfterSave.getRoadmapTopic().getId();
        updateInfoAboutQuestion(roadmapTopicId, getAllByTopicId(roadmapTopicId, Pageable.unpaged()));

        return roadmapQuestionMapper.toResponseDto(roadmapQuestionAfterSave);
    }

    @Transactional
    @Override
    public RoadmapQuestionResponseDto patch(@NonNull Long id, @NonNull RoadmapQuestionRequestDto roadmapQuestionRequestDto) {
        Optional.ofNullable(roadmapQuestionRequestDto.getRoadmapTopicId())
                .ifPresent(this::checkRoadmapTopicForAvailability);

        var roadmapQuestionForUpdate = getEntityById(id);
        var roadmapQuestion = roadmapQuestionMapper.toPatchEntity(roadmapQuestionRequestDto, roadmapQuestionForUpdate);
        recalculatePositions(roadmapQuestion);

        var roadmapTopicId = roadmapQuestion.getRoadmapTopic().getId();
        updateInfoAboutQuestion(roadmapTopicId, getAllByTopicId(roadmapTopicId, Pageable.unpaged()));

        return roadmapQuestionMapper.toResponseDto(roadmapQuestion);
    }

    @Transactional(readOnly = true)
    @Override
    public RoadmapQuestion getEntityById(@NonNull Long id) {
        return roadmapQuestionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(QUESTION_WITH_ID_NOT_FOUND, id)));
    }

    @Transactional(readOnly = true)
    @Override
    public RoadmapQuestionResponseDto getResponseById(@NonNull Long id) {
        return roadmapQuestionMapper.toResponseDto(getEntityById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RoadmapQuestionResponseDto> getAll(@NonNull RoadmapQuestionFilter roadmapQuestionFilter, @NonNull Pageable pageable) {
        return roadmapQuestionRepository.findAll(RoadmapQuestionSpecification.filterBy(roadmapQuestionFilter), pageable)
                .map(roadmapQuestionMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoadmapQuestion> getAllEntity(@NonNull RoadmapQuestionFilter roadmapQuestionFilter, Sort sort) {
        return roadmapQuestionRepository.findAll(RoadmapQuestionSpecification.filterBy(roadmapQuestionFilter), sort);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RoadmapQuestionResponseDto> getAllByTopicId(@NonNull Long id, @NonNull Pageable pageable) {
        return roadmapQuestionRepository.findAllByRoadmapTopicId(id, pageable)
                .map(roadmapQuestionMapper::toResponseDto);
    }

    @Transactional
    @Override
    public void deleteById(@NonNull Long id) {
        var roadmapQuestion = getEntityById(id);

        roadmapQuestionRepository.delete(roadmapQuestion);

        var roadmapTopicId = roadmapQuestion.getRoadmapTopic().getId();
        updateInfoAboutQuestion(roadmapTopicId, getAllByTopicId(roadmapTopicId, Pageable.unpaged()));
    }

    private void checkRoadmapTopicForAvailability(Long roadmapTopicId) {
        roadmapTopicService.getEntityById(roadmapTopicId);
    }

    private void updateInfoAboutQuestion(Long roadmapTopicId, Page<RoadmapQuestionResponseDto> questions) {
        roadmapTopicService.updateNumberOfQuestion(roadmapTopicId, questions.getNumberOfElements());
        roadmapTopicService.updateNumberExploredQuestion(roadmapTopicId, questions.getContent().stream()
                .filter(filter -> filter.getIsExplored().equals(Boolean.TRUE))
                .count());
    }

    @Transactional
    @Override
    public void positionDefinition(RoadmapQuestion roadmapQuestion) {
        Optional.ofNullable(roadmapQuestion.getPosition()).ifPresentOrElse(
                roadmapQuestion::setPosition,
                () -> {
                    var roadmapQuestions = new LinkedList<>(getAllEntity(RoadmapQuestionFilter.builder()
                            .roadmapTopicIds(List.of(roadmapQuestion.getRoadmapTopic().getId()))
                            .build(), Sort.by(Sort.Direction.ASC, "position")));

                    if (!CollectionUtils.isEmpty(roadmapQuestions)) {
                        roadmapQuestion.setPosition(roadmapQuestions.getLast().getPosition() + 1);
                    } else {
                        roadmapQuestion.setPosition(1L);
                    }
                });
    }

    @Transactional
    @Override
    public void recalculatePositions(RoadmapQuestion roadmapQuestion) {
        var roadmapQuestions = new LinkedList<>(getAllEntity(RoadmapQuestionFilter.builder()
                .roadmapTopicIds(List.of(roadmapQuestion.getRoadmapTopic().getId()))
                .build(), Sort.by(Sort.Direction.ASC, "position")));

        for (int count = 0; count < roadmapQuestions.size(); count++) {
            roadmapQuestions.get(count).setPosition((long) count);
        }

        roadmapQuestionRepository.saveAll(roadmapQuestions);
    }
}
