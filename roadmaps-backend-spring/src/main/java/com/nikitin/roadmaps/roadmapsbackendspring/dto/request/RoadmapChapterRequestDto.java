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

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = " ")
public class RoadmapChapterRequestDto {

    @Schema(description = "Название раздела", example = "Java Core")
    @NotNull(message = "Поле <name> не может быть null", groups = Create.class)
    @NotBlank(message = "Поле <name> должно содержать значение", allowNull = true, groups = { Create.class, Patch.class })
    private String name;

    @Schema(description = "Позиция раздела", example = "4")
    private Long position;

    @Schema(description = "Идентификатор карты развития", example = "1")
    @NotNull(message = "Поле <roadmapId> не может быть null", groups = Create.class)
    @Positive(message = "Идентификатор не может быть отрицательным значением", groups = { Create.class, Patch.class })
    private Long roadmapId;
}
