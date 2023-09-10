package com.nikitin.roadmaps.dto.response.pageable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nikitin.roadmaps.dto.response.RoadmapTopicResponseDto;
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
public class PageableRoadmapTopicResponseDto extends Pageable {

    @JsonProperty("content")
    private List<RoadmapTopicResponseDto> roadmapTopicResponseDtos;
}