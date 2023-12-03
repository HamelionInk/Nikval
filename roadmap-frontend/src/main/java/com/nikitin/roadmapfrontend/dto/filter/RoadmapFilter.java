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
public class RoadmapFilter {

    private List<Long> ids;
    private String startWithName;
    private List<Long> profileIds;
    private Boolean custom;
}
