package com.nikitin.roadmaps.roadmapsbackendspring.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
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

	@Schema(description = "Фильтр по идентификатору в базе данных", example = "5")
	private List<Long> ids;

	@Schema(description = "Фильтр по идентификатору карты развития", example = "[\"4\", \"2\"]")
	private List<Long> roadmapIds;

	@Schema(description = "Фильтр по наименованию раздела", example = "Java")
	private String startWithName;
}
