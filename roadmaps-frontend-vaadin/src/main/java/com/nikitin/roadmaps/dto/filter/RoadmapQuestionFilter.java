package com.nikitin.roadmaps.dto.filter;

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
public class RoadmapQuestionFilter {

    private List<Long> ids;
    private List<Long> roadmapTopicIds;
    private String startWithQuestion;
    private String startWithAnswer;
    private Boolean isExplored;
}
