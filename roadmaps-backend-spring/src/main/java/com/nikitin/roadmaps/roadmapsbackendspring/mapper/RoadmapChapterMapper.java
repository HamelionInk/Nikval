package com.nikitin.roadmaps.roadmapsbackendspring.mapper;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Profile;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Roadmap;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter;
import com.nikitin.roadmaps.roadmapsbackendspring.service.RoadmapService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = RoadmapTopicMapper.class)
public abstract class RoadmapChapterMapper {

    @Mapping(source = "dto.roadmapTopicRequestDtos", target = "roadmapTopics")
    public abstract RoadmapChapter toEntity(RoadmapChapterRequestDto dto);

    public abstract RoadmapChapter toPatchEntity(RoadmapChapterRequestDto dto, @MappingTarget RoadmapChapter entityForUpdate);

    @Mapping(source = "entity.roadmapTopics", target = "roadmapTopicResponseDtos")
    public abstract RoadmapChapterResponseDto toResponseDto(RoadmapChapter entity);

    @AfterMapping
    protected void setRoadmapChapter(@MappingTarget RoadmapChapter entity) {
        Optional.ofNullable(entity.getRoadmapTopics())
                .ifPresent(roadmapTopics ->
                        roadmapTopics.forEach(roadmapTopic ->
                                roadmapTopic.setRoadmapChapter(entity)));
    }

    @AfterMapping
    protected void setRoadmapChapterId(@MappingTarget RoadmapChapterResponseDto dto) {
        Optional.ofNullable(dto.getRoadmapTopicResponseDtos())
                .ifPresent(roadmapTopicResponseDtos ->
                        roadmapTopicResponseDtos.forEach(roadmapTopicResponseDto ->
                                roadmapTopicResponseDto.setRoadmapChapterId(dto.getId())));
    }
}
