package com.nikitin.roadmapfrontend.dto.filter;

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
public class RoadmapTopicFilter {

    private List<Long> roadmapChapterIds;
    private String startWithName;
}
