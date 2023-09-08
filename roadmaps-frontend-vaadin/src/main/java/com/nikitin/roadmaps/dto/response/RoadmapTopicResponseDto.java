package com.nikitin.roadmaps.dto.response;

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
public class RoadmapTopicResponseDto {

    private Long id;
    private String name;
    private Integer numberOfQuestion;
    private Integer numberExploredQuestion;
    private Long roadmapChapterId;
    private List<RoadmapQuestionResponseDto> roadmapQuestionResponseDtos;
}
