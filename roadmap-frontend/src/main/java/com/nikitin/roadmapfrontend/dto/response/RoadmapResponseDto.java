package com.nikitin.roadmapfrontend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoadmapResponseDto {

    private Long id;
    private String name;
    private Long profileId;
    private Boolean custom;
    private Boolean favorite;
}
