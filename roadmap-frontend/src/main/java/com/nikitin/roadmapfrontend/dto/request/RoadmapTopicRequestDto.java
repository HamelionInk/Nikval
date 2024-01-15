package com.nikitin.roadmapfrontend.dto.request;

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
public class RoadmapTopicRequestDto {

    private String name;
    private Long position;
    private Long roadmapChapterId;
}
