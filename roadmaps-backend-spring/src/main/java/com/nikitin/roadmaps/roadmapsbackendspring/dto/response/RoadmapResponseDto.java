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
public class RoadmapResponseDto {

    @Schema(description = "Идентификатор в базе данных", example = "13")
    private Long id;

    @Schema(description = "Название карты развития", example = "Java backend")
    private String name;

    @Schema(description = "Идентификатор кому пренадлежит карта развития", example = "5")
    private Long profileId;

    @Schema(description = "Флаг стандартной карты или созданной пользователем")
    private Boolean custom;

    @Schema(description = "Флаг показывающий избранность карты развития", example = "false")
    private Boolean favorite;
}
