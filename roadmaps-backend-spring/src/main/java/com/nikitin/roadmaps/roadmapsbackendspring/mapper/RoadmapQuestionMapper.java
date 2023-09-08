package com.nikitin.roadmaps.roadmapsbackendspring.mapper;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class RoadmapQuestionMapper {

    public abstract RoadmapQuestion toEntity(RoadmapQuestionRequestDto dto);

    public abstract RoadmapQuestion toPatchEntity(RoadmapQuestionRequestDto dto, @MappingTarget RoadmapQuestion entityForUpdate);

    public abstract RoadmapQuestionResponseDto toResponseDto(RoadmapQuestion entity);
}
