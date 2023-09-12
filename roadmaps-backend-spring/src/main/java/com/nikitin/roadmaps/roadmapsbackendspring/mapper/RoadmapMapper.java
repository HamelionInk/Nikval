package com.nikitin.roadmaps.roadmapsbackendspring.mapper;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.RoadmapRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Profile;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Roadmap;
import com.nikitin.roadmaps.roadmapsbackendspring.service.ProfileService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class RoadmapMapper {

    @Autowired
    private ProfileService profileService;

    @Mapping(source = "dto.profileId", target = "profile", qualifiedByName = "converterToEntityProfile")
    public abstract Roadmap toEntity(RoadmapRequestDto dto);

    @Mapping(source = "dto.profileId", target = "profile", qualifiedByName = "converterToEntityProfile")
    public abstract Roadmap toPatchEntity(RoadmapRequestDto dto, @MappingTarget Roadmap entityForUpdate);

    @Mapping(source = "entity.profile", target = "profileId", qualifiedByName = "converterToResponseDtoProfileId")
    public abstract RoadmapResponseDto toResponseDto(Roadmap entity);

    @Named("converterToEntityProfile")
    protected Profile converterToEntityProfile(Long profileId) {
        return profileService.getEntityById(profileId);
    }

    @Named("converterToResponseDtoProfileId")
    protected Long converterToResponseDtoProfileId(Profile profile) {
        return profile.getId();
    }
}
