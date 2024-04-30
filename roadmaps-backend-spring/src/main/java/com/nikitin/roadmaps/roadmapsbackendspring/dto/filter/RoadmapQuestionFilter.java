package com.nikitin.roadmaps.roadmapsbackendspring.dto.filter;

import com.nikitin.roadmaps.roadmapsbackendspring.utils.enums.ExploredStatus;
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
public class RoadmapQuestionFilter {

    @Schema(description = "Фильтр по идентификаторам в базе данных", example = "56")
    private List<Long> ids;

    @Schema(description = "Фильтр по по идентификаторам темы")
    private List<Long> roadmapTopicIds;

    @Schema(description = "Фильтр по названию вопроса", example = "Для чего используются Stream API")
    private String startWithQuestion;

    @Schema(description = "Фильтр по названию ответа на вопрос", example = "Для прогрессивной работы с коллекциями")
    private String startWithAnswer;

    @Schema(description = "Фильтр по cтатусу изучения вопроса", example = "[\"IN_PROGRESS_EXPLORED\", \"NOT_EXPLORED\"]")
    private List<ExploredStatus> exploredStatuses;
}
