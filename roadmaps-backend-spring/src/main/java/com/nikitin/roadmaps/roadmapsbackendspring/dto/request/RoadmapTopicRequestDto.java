package com.nikitin.roadmaps.roadmapsbackendspring.dto.request;

import com.nikitin.roadmaps.roadmapsbackendspring.validation.Create;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Patch;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.annotation.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
@Schema(description = "")
public class RoadmapTopicRequestDto {

    @Schema(description = "Название темы", example = "Stream API")
    @NotNull(message = "Поле <name> не может быть null", groups = Create.class)
    @NotBlank(message = "Поле <name> должно иметь значение", allowNull = true, groups = { Create.class, Patch.class })
    private String name;

    @Schema(description = "Позиция темы", example = "4")
    private Long position;

    @Schema(description = "Количество вопросов в теме", example = "15")
    @Positive(message = "Поле <numberOfQuestion> не может быть отрицательным значением", groups = { Create.class, Patch.class })
    private Long numberOfQuestion;

    @Schema(description = "Количество изученных вопросов", example = "3")
    @Positive(message = "Поле <numberExploredQuestion> не может быть отрицательным значением", groups = { Create.class, Patch.class })
    private Long numberExploredQuestion;

    @Schema(description = "Идентификатор раздела", example = "34")
    @NotNull(message = "Поле <roadmapChapterId> не может быть null", groups = Create.class)
    @Positive(message = "Идентификатор не может быть отрицательным значением", groups = { Create.class, Patch.class })
    private Long roadmapChapterId;
}
