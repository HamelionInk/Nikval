package com.nikitin.roadmaps.roadmapsbackendspring.mapper;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Profile;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Roadmap;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapChapterService;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class RoadmapChapterMapper {

    @Autowired
    private RoadmapService roadmapService;

    @Mapping(source = "dto.roadmapId", target = "roadmap", qualifiedByName = "converterToEntityRoadmap")
    public abstract RoadmapChapter toEntity(RoadmapChapterRequestDto dto);

    @Mapping(source = "dto.roadmapId", target = "roadmap", qualifiedByName = "converterToEntityRoadmap")
    public abstract RoadmapChapter toPatchEntity(RoadmapChapterRequestDto dto, @MappingTarget RoadmapChapter entityForUpdate);

    @Mapping(source = "entity.roadmap", target = "roadmapId", qualifiedByName = "converterToResponseDtoRoadmapId")
    public abstract RoadmapChapterResponseDto toResponseDto(RoadmapChapter entity);

    @Named("converterToEntityRoadmap")
    protected Roadmap converterToEntityRoadmap(Long roadmapId) {
        return roadmapService.getEntityById(roadmapId);
    }

    @Named("converterToResponseDtoRoadmapId")
    protected Long converterToResponseDtoRoadmapId(Roadmap roadmap) {
        return roadmap.getId();
    }
}
