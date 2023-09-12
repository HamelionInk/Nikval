package com.nikitin.roadmaps.dto.request;

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
public class RoadmapQuestionRequestDto {

    private String question;
    private String answer;
    private Boolean isExplored;
    private Long roadmapTopicId;
}
