package com.nikitin.roadmapfrontend.dto.request;

import com.nikitin.roadmapfrontend.utils.enums.ExploredStatus;
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
    private ExploredStatus exploredStatus;
    private Long roadmapTopicId;
    private Long position;
}
