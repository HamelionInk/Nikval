package com.nikitin.roadmaps.roadmapsbackendspring.mapper;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapQuestion;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapTopicService;
import com.nikitin.roadmaps.roadmapsbackendspring.utils.enums.ExploredStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class RoadmapQuestionMapper {

    @Autowired
    private RoadmapTopicService roadmapTopicService;

    @Mapping(source = "dto.roadmapTopicId", target = "roadmapTopic", qualifiedByName = "converterToEntityRoadmapTopic")
    public abstract RoadmapQuestion toEntity(RoadmapQuestionRequestDto dto);

    @Mapping(source = "dto.roadmapTopicId", target = "roadmapTopic", qualifiedByName = "converterToEntityRoadmapTopic")
    public abstract RoadmapQuestion toPatchEntity(RoadmapQuestionRequestDto dto, @MappingTarget RoadmapQuestion entityForUpdate);

    @Mapping(source = "entity.roadmapTopic", target = "roadmapTopicId", qualifiedByName = "converterToResponseDtoRoadmapTopicId")
    public abstract RoadmapQuestionResponseDto toResponseDto(RoadmapQuestion entity);

    @Named("converterToEntityRoadmapTopic")
    protected RoadmapTopic converterToEntityRoadmapTopic(Long roadmapTopicId) {
        return roadmapTopicService.getEntityById(roadmapTopicId);
    }

    @Named("converterToResponseDtoRoadmapTopicId")
    protected Long converterToResponseDtoRoadmapTopicId(RoadmapTopic roadmapTopic) {
        return roadmapTopic.getId();
    }
}
