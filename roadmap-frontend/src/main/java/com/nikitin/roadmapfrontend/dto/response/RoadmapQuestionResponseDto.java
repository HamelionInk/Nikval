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
public class RoadmapQuestionResponseDto {

    private Long id;
    private String question;
    private String answer;
    private Boolean isExplored;
    private Long roadmapTopicId;
}
