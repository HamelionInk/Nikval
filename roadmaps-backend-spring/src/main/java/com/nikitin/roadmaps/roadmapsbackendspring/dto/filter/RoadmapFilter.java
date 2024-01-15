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
public class RoadmapFilter {

    @Schema(description = "Фильтр по идентификатору в базе данных", example = "13")
    private List<Long> ids;

    @Schema(description = "Фильтр по название карты развития", example = "Java backend")
    private String startWithName;

    @Schema(description = "Фильтр по идентификатор кому пренадлежит карта развития", example = "5")
    private List<Long> profileIds;

    @Schema(description = "Фильтр по флагу стандартной карты или созданной пользователем", example = "true")
    private Boolean custom;

    @Schema(description = "Фильтр по флагу избранности карты", example = "true")
    private Boolean favorite;
}
