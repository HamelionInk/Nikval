package com.nikitin.roadmapfrontend.dto.data;

import com.nikitin.roadmapfrontend.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapResponseDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapTopicResponseDto;
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
public class RoadmapData {

	private RoadmapResponseDto roadmap;
	private List<RoadmapChapterResponseDto> chapters;
	private List<RoadmapTopicResponseDto> topics;
}
