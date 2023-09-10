package com.nikitin.roadmaps.roadmapsbackendspring.mapper;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Optional;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = RoadmapQuestionMapper.class)
public abstract class RoadmapTopicMapper {

    @Mapping(source = "dto.roadmapQuestionRequestDtos", target = "roadmapQuestions")
    public abstract RoadmapTopic toEntity(RoadmapTopicRequestDto dto);

    @Mapping(source = "dto.roadmapQuestionRequestDtos", target = "roadmapQuestions")
    public abstract RoadmapTopic toPatchEntity(RoadmapTopicRequestDto dto, @MappingTarget RoadmapTopic entityForUpdate);

    @Mapping(source = "entity.roadmapQuestions", target = "roadmapQuestionResponseDtos")
    public abstract RoadmapTopicResponseDto toResponseDto(RoadmapTopic entity);

    @AfterMapping
    protected void setRoadmapTopic(@MappingTarget RoadmapTopic entity) {
        Optional.ofNullable(entity.getRoadmapQuestions())
                .ifPresent(roadmapQuestions ->
                        roadmapQuestions.forEach(roadmapQuestion ->
                                roadmapQuestion.setRoadmapTopic(entity)));
    }

    @AfterMapping
    protected void setRoadmapTopicId(@MappingTarget RoadmapTopicResponseDto dto) {
        Optional.ofNullable(dto.getRoadmapQuestionResponseDtos())
                .ifPresent(roadmapQuestionResponseDtos ->
                        roadmapQuestionResponseDtos.forEach(roadmapQuestionResponseDto ->
                                roadmapQuestionResponseDto.setRoadmapTopicId(dto.getId())));
    }

    @AfterMapping
    protected void setInfoOfQuestions(@MappingTarget RoadmapTopic entity) {
        Optional.ofNullable(entity.getRoadmapQuestions())
                .ifPresent(roadmapQuestions -> {
                    roadmapQuestions.forEach(roadmapQuestion -> {
                        if (roadmapQuestion.getIsExplored()) {
                            entity.incrementExploredQuestion();
                        }
                    });
                    entity.setNumberOfQuestion(roadmapQuestions.size());
                });
    }
}
