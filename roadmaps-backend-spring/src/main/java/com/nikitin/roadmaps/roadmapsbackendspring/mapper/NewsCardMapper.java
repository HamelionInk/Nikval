package com.nikitin.roadmaps.roadmapsbackendspring.mapper;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.NewsCardRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.NewsCardResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.NewsCard;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class NewsCardMapper {

    public abstract NewsCard toEntity(NewsCardRequestDto dto);

    public abstract NewsCard toPatchEntity(NewsCardRequestDto dto, @MappingTarget NewsCard entityForUpdate);

    public abstract NewsCardResponseDto toResponseDto(NewsCard entity);
}
