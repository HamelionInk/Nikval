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
public class RoadmapTopicFilter {

    @Schema(description = "Фильтр по идентификатору раздела", example = "4")
    private List<Long> roadmapChapterIds;

    @Schema(description = "Фильтр по имени темы", example = "Stream API")
    private String startWithName;
}
