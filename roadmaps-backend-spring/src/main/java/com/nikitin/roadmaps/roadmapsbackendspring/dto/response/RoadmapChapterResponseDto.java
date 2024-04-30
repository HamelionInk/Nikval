package com.nikitin.roadmaps.roadmapsbackendspring.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "")
public class RoadmapChapterResponseDto {

	@Schema(description = "Идентификатор в базе данных", example = "54")
	private Long id;

	@Schema(description = "Название раздела", example = "Java Core")
	private String name;

	@Schema(description = "Позиция раздела", example = "4")
	private Long position;

	@Schema(description = "Идентификатор отношения к карте развития", example = "12")
	private Long roadmapId;
}
