package com.nikitin.roadmaps.dto.response.pageable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nikitin.roadmaps.dto.response.RoadmapResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageableRoadmapResponseDto extends Pageable {

    @JsonProperty("content")
    private List<RoadmapResponseDto> roadmapResponseDtos;
}
