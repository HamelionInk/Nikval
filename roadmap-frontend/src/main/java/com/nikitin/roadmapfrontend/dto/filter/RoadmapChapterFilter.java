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
public class RoadmapChapterFilter {

	private List<Long> ids;
	private List<Long> roadmapIds;
	private String startWithName;
	private Long page;
	private Long size;
}
