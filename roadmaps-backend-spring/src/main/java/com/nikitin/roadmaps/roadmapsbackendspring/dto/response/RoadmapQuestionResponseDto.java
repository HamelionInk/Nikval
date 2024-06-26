package com.nikitin.roadmaps.roadmapsbackendspring.dto.response;

import com.nikitin.roadmaps.roadmapsbackendspring.utils.enums.ExploredStatus;
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
public class RoadmapQuestionResponseDto {

    @Schema(description = "Идентификатор в базе данных", example = "56")
    private Long id;

    @Schema(description = "Название вопроса", example = "Для чего используются Stream API")
    private String question;

    @Schema(description = "Ответ на вопрос", example = "Для прогрессивной работы с коллекциями")
    private String answer;

    @Schema(description = "Статус изучения вопроса", example = "IN_PROGRESS_EXPLORED")
    private ExploredStatus exploredStatus;

    @Schema(description = "Позиция вопроса", example = "4")
    private Long position;

    @Schema(description = "Идентификатор отношения к теме", example = "29")
    private Long roadmapTopicId;
}
