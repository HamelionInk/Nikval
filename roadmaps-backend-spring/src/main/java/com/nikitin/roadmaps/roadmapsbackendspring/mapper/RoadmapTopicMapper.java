package com.nikitin.roadmaps.roadmapsbackendspring.mapper;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapChapterService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class RoadmapTopicMapper {

    @Autowired
    private RoadmapChapterService roadmapChapterService;

    @Mapping(source = "dto.roadmapChapterId", target = "roadmapChapter", qualifiedByName = "converterToEntityRoadmapChapter")
    public abstract RoadmapTopic toEntity(RoadmapTopicRequestDto dto);

    @Mapping(source = "dto.roadmapChapterId", target = "roadmapChapter", qualifiedByName = "converterToEntityRoadmapChapter")
    public abstract RoadmapTopic toPatchEntity(RoadmapTopicRequestDto dto, @MappingTarget RoadmapTopic entityForUpdate);

    @Mapping(source = "entity.roadmapChapter", target = "roadmapChapterId", qualifiedByName = "converterToResponseDtoRoadmapChapterId")
    public abstract RoadmapTopicResponseDto toResponseDto(RoadmapTopic entity);

    @Named("converterToEntityRoadmapChapter")
    protected RoadmapChapter converterToEntityRoadmapChapter(Long roadmapChapterId) {
        return roadmapChapterService.getEntityById(roadmapChapterId);
    }

    @Named("converterToResponseDtoRoadmapChapterId")
    protected Long converterToResponseDtoRoadmapChapterId(RoadmapChapter roadmapChapter) {
        return roadmapChapter.getId();
    }
}
