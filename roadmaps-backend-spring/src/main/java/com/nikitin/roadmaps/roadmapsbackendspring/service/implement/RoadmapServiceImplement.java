package com.nikitin.roadmaps.roadmapsbackendspring.service.implement;

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
import com.nikitin.roadmaps.roadmapsbackendspring.exception.NotFoundException;
import com.nikitin.roadmaps.roadmapsbackendspring.mapper.RoadmapChapterMapper;
import com.nikitin.roadmaps.roadmapsbackendspring.mapper.RoadmapMapper;
import com.nikitin.roadmaps.roadmapsbackendspring.mapper.RoadmapQuestionMapper;
import com.nikitin.roadmaps.roadmapsbackendspring.mapper.RoadmapTopicMapper;
import com.nikitin.roadmaps.roadmapsbackendspring.repository.RoadmapChapterRepository;
import com.nikitin.roadmaps.roadmapsbackendspring.repository.RoadmapQuestionRepository;
import com.nikitin.roadmaps.roadmapsbackendspring.repository.RoadmapRepository;
import com.nikitin.roadmaps.roadmapsbackendspring.repository.RoadmapTopicRepository;
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
    private static final String CHAPTER_WITH_ID_NOT_FOUND = "Раздел с указанным идентификатором не найден - %s";
    private static final String TOPIC_WITH_ID_NOT_FOUND = "Тема с указанным идентификатором не найдена - %s";
    private static final String QUESTION_WITH_ID_NOT_FOUND = "Вопрос с указанным идентификатором не найден - %s";

    private final ProfileService profileService;
    private final RoadmapRepository roadmapRepository;
    private final RoadmapChapterRepository roadmapChapterRepository;
    private final RoadmapTopicRepository roadmapTopicRepository;
    private final RoadmapQuestionRepository roadmapQuestionRepository;
    private final RoadmapMapper roadmapMapper;
    private final RoadmapChapterMapper roadmapChapterMapper;
    private final RoadmapTopicMapper roadmapTopicMapper;
    private final RoadmapQuestionMapper roadmapQuestionMapper;

    @Override
    public RoadmapResponseDto create(@NonNull RoadmapRequestDto roadmapRequestDto) {
        Optional.ofNullable(profileService.getEntityById(roadmapRequestDto.getProfileId()))
                .orElseThrow(() -> new NotFoundException(String.format(PROFILE_WITH_ID_NOT_FOUND, roadmapRequestDto.getProfileId())));

        var roadmap = roadmapMapper.toEntity(roadmapRequestDto);

        return roadmapMapper.toResponseDto(roadmapRepository.save(roadmap));
    }

    @Override
    public RoadmapResponseDto createChapter(@NonNull Long roadmapId, @NonNull RoadmapChapterRequestDto roadmapChapterRequestDto) {
        var roadmap = getEntityById(roadmapId);
        var roadmapChapter = roadmapChapterMapper.toEntity(roadmapChapterRequestDto);

        roadmap.addRoadmapChapter(roadmapChapter);

        return roadmapMapper.toResponseDto(roadmapRepository.save(roadmap));
    }

    @Override
    public RoadmapChapterResponseDto createTopic(@NonNull Long chapterId, @NonNull RoadmapTopicRequestDto roadmapTopicRequestDto) {
        var chapter = getEntityChapterById(chapterId);
        var roadmapTopic = roadmapTopicMapper.toEntity(roadmapTopicRequestDto);

        chapter.addRoadmapTopic(roadmapTopic);

        return roadmapChapterMapper.toResponseDto(roadmapChapterRepository.save(chapter));
    }

    @Override
    public RoadmapTopicResponseDto createQuestion(@NonNull Long topicId, @NonNull RoadmapQuestionRequestDto roadmapQuestionRequestDto) {
        var topic = getEntityTopicById(topicId);
        var roadmapQuestion = roadmapQuestionMapper.toEntity(roadmapQuestionRequestDto);

        topic.addRoadmapQuestion(roadmapQuestion);
        topic.incrementNumberOfQuestion();
        if(roadmapQuestion.getIsExplored()) {
            topic.incrementExploredQuestion();
        }
        return roadmapTopicMapper.toResponseDto(roadmapTopicRepository.save(topic));
    }

    @Override
    public RoadmapResponseDto patch(@NonNull Long id, @NonNull RoadmapRequestDto roadmapRequestDto) {
        var roadmapForUpdate = getEntityById(id);

        var roadmap = roadmapMapper.toPatchEntity(roadmapRequestDto, roadmapForUpdate);
        return roadmapMapper.toResponseDto(roadmapRepository.save(roadmap));
    }

    @Override
    public RoadmapTopicResponseDto patchTopicById(@NonNull Long id, @NonNull RoadmapTopicRequestDto roadmapTopicRequestDto) {
        var roadmapTopicForUpdate = getEntityTopicById(id);

        var roadmapTopic = roadmapTopicMapper.toPatchEntity(roadmapTopicRequestDto, roadmapTopicForUpdate);
        return roadmapTopicMapper.toResponseDto(roadmapTopicRepository.save(roadmapTopic));
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
    public RoadmapChapter getEntityChapterById(@NonNull Long id) {
        return roadmapChapterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(CHAPTER_WITH_ID_NOT_FOUND, id)));
    }

    @Override
    public RoadmapChapterResponseDto getResponseChapterById(@NonNull Long id) {
        return null;
    }

    @Override
    public RoadmapTopic getEntityTopicById(@NonNull Long id) {
        return roadmapTopicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(TOPIC_WITH_ID_NOT_FOUND, id)));
    }

    @Override
    public RoadmapTopicResponseDto getResponseTopicById(@NonNull Long id) {
        return null;
    }

    @Override
    public RoadmapQuestion getEntityQuestionById(@NonNull Long id) {
        return roadmapQuestionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(QUESTION_WITH_ID_NOT_FOUND, id)));
    }

    @Override
    public RoadmapQuestionResponseDto getResponseQuestionById(@NonNull Long id) {
        return null;
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

    @Override
    public Page<RoadmapTopicResponseDto> getAllTopicByChapterId(@NonNull Long id, @NonNull Pageable pageable) {
        return roadmapTopicRepository.findAllByRoadmapChapterId(id, pageable)
                .map(roadmapTopicMapper::toResponseDto);
    }

    @Override
    public Page<RoadmapQuestionResponseDto> getAllQuestionByTopicId(@NonNull Long id, @NonNull Pageable pageable) {
        return roadmapQuestionRepository.findAllByRoadmapTopicId(id, pageable)
                .map(roadmapQuestionMapper::toResponseDto);
    }

    @Override
    public void deleteRoadmapById(@NonNull Long id) {
        var roadmap = getEntityById(id);

        roadmapRepository.delete(roadmap);
    }

    @Override
    public void deleteChapterById(@NonNull Long id) {
        var roadmapChapter = getEntityChapterById(id);

        roadmapChapterRepository.delete(roadmapChapter);
    }

    @Override
    public void deleteTopicById(@NonNull Long id) {
        var roadmapTopic = getEntityTopicById(id);

        roadmapTopicRepository.delete(roadmapTopic);
    }

    @Override
    public void deleteQuestionById(@NonNull Long id) {
        var roadmapQuestion = getEntityQuestionById(id);
        var roadmapTopic = getEntityTopicById(roadmapQuestion.getRoadmapTopic().getId());
        roadmapTopic.decrementNumberOfQuestion();

        roadmapTopicRepository.save(roadmapTopic);

        roadmapQuestionRepository.delete(roadmapQuestion);
    }
}
