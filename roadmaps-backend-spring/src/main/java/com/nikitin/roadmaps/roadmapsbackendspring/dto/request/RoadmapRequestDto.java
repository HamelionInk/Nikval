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

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "")
public class RoadmapRequestDto {

    @Schema(description = "Название карты развития", example = "Java backend")
    @NotNull(message = "Поле <name> не может быть null", groups = Create.class)
    @NotBlank(message = "Поле <name> должно иметь значение", allowNull = true, groups = { Create.class, Patch.class })
    private String name;

    @Schema(description = "Идентификатор кому пренадлежит карта развития", example = "5")
    @NotNull(message = "Поле <profileId> не может быть null", groups = Create.class)
    @Positive(message = "Идентификатор не может быть отрицательным значением", groups = { Create.class, Patch.class })
    private Long profileId;

    @Schema(description = "Флаг показывающий создание пользовательской карты развития", example = "true")
    @NotNull(message = "Поле <custom> не может быть null", groups = Create.class)
    private Boolean custom;

    @Schema(description = "Флаг показывающий избранность карты развития", example = "false")
    @NotNull(message = "Поле <favorite> не может быть null")
    private Boolean favorite;
}
