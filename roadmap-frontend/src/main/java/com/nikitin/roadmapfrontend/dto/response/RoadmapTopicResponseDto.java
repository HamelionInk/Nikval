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
public class RoadmapTopicResponseDto {

    private Long id;
    private String name;
    private Integer numberOfQuestion;
    private Integer numberExploredQuestion;
    private Long roadmapChapterId;
}
