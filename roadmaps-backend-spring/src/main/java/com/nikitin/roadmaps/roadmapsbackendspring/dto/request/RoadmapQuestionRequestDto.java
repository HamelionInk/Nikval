package com.nikitin.roadmaps.roadmapsbackendspring.dto.request;

import com.nikitin.roadmaps.roadmapsbackendspring.validation.Create;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Patch;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.annotation.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "")
public class RoadmapQuestionRequestDto {

    @Schema(description = "Название вопроса", example = "Для чего используются Stream API")
    @NotNull(message = "Поле <question> не может быть null", groups = Create.class)
    @NotBlank(message = "Поле <question> должно иметь значение", allowNull = true, groups = { Create.class, Patch.class })
    private String question;

    @Schema(description = "Ответ на вопрос", example = "Для прогрессивной работы с коллекциями")
    @NotNull(message = "Поле <answer> не может быть null", groups = Create.class)
    @NotBlank(message = "Поле <answer> должно иметь значение", allowNull = true, groups = { Create.class, Patch.class })
    private String answer;

    @Schema(description = "Состояние, показывающее изучен вопрос или нет", example = "false")
    @NotNull(message = "Поле <isExplored> не может быть null", groups = Create.class)
    private Boolean isExplored;

    @Schema(description = "Идентификатор темы", example = "43")
    @NotNull(message = "Поле <roadmapTopicId> не может быть null", groups = Create.class)
    @Positive(message = "Идентификатор не может быть отрицательным значением", groups = { Create.class, Patch.class })
    private Long roadmapTopicId;
}
