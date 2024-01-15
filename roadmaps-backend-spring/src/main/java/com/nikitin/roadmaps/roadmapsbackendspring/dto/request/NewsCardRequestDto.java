package com.nikitin.roadmaps.roadmapsbackendspring.dto.request;

import com.nikitin.roadmaps.roadmapsbackendspring.validation.Create;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Patch;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.annotation.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO запроса NewsCard")
public class NewsCardRequestDto {

    @Schema(description = "Заголовок", example = "")
    @NotBlank(message = "Поле <title> должно содержать значение", allowNull = true, groups = {Create.class, Patch.class})
    private String title;

    @Schema(description = "URL адрес хранения изображения", example = "")
    @NotBlank(message = "Поле <image> должно содержать значение", allowNull = true, groups = {Patch.class})
    private String image;

    @Schema(description = "Описание", example = "")
    @NotBlank(message = "Поле <description> должно содержать значение", allowNull = true, groups = {Create.class, Patch.class})
    private String description;

    @Schema(description = "Время публикации новости", example = "")
    @NotNull(message = "Поле <createdAt> не может быть пустым", groups = Create.class)
    private LocalDateTime createdAt;
}
