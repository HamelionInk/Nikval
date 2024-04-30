package com.nikitin.roadmapfrontend.dto.filter;

import com.nikitin.roadmapfrontend.utils.enums.ExploredStatus;
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
    private List<ExploredStatus> exploredStatuses;
}
