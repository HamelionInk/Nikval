package com.nikitin.roadmaps.roadmapsbackendspring.mapper;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapQuestion;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class RoadmapQuestionMapper {

    public abstract RoadmapQuestion toEntity(RoadmapQuestionRequestDto dto);

    public abstract RoadmapQuestion toPatchEntity(RoadmapQuestionRequestDto dto, @MappingTarget RoadmapQuestion entityForUpdate);

    @Mapping(source = "entity.roadmapTopic", target = "roadmapTopicId", qualifiedByName = "setRoadmapTopicId")
    public abstract RoadmapQuestionResponseDto toResponseDto(RoadmapQuestion entity);

    @Named("setRoadmapTopicId")
    protected Long setRoadmapTopicId(RoadmapTopic roadmapTopic) {
        return roadmapTopic.getId();
    }
}
