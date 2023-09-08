package com.nikitin.roadmaps.roadmapsbackendspring.mapper;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Profile;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Roadmap;
import com.nikitin.roadmaps.roadmapsbackendspring.service.ProfileService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = RoadmapChapterMapper.class)
public abstract class RoadmapMapper {

    @Autowired
    private ProfileService profileService;

    @Mapping(source = "dto.roadmapChapterRequestDtos", target = "roadmapChapters")
    @Mapping(source = "dto.profileId", target = "profile", qualifiedByName = "setProfile")
    public abstract Roadmap toEntity(RoadmapRequestDto dto);

    @Mapping(source = "dto.roadmapChapterRequestDtos", target = "roadmapChapters")
    @Mapping(source = "dto.profileId", target = "profile", qualifiedByName = "setProfile")
    public abstract Roadmap toPatchEntity(RoadmapRequestDto dto, @MappingTarget Roadmap entityForUpdate);

    @Mapping(source = "entity.roadmapChapters", target = "roadmapChapterResponseDtos")
    @Mapping(source = "entity.profile", target = "profileId", qualifiedByName = "setProfileId")
    public abstract RoadmapResponseDto toResponseDto(Roadmap entity);

    @Named("setProfile")
    protected Profile setProfile(Long profileId) {
        return profileService.getEntityById(profileId);
    }

    @Named("setProfileId")
    protected Long setProfileId(Profile profile) {
        return profile.getId();
    }

    @AfterMapping
    protected void setRoadmap(@MappingTarget Roadmap entity) {
        Optional.ofNullable(entity.getRoadmapChapters())
                .ifPresent(roadmapChapters ->
                        roadmapChapters.forEach(roadmapChapter ->
                                roadmapChapter.setRoadmap(entity)
                        ));
    }

    @AfterMapping
    protected void setRoadmapId(@MappingTarget RoadmapResponseDto dto) {
        Optional.ofNullable(dto.getRoadmapChapterResponseDtos())
                .ifPresent(roadmapChapterResponseDtos ->
                        roadmapChapterResponseDtos.forEach(roadmapChapterResponseDto ->
                                roadmapChapterResponseDto.setRoadmapId(dto.getId())));
    }
}
