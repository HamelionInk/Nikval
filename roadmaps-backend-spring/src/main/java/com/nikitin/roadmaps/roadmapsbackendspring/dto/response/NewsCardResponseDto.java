package com.nikitin.roadmaps.roadmapsbackendspring.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO ответа NewsCard")
public class NewsCardResponseDto {

    @Schema(description = "Идентификатор в базе данных", example = "4")
    private Long id;

    @Schema(description = "Заголовок", example = "")
    private String title;

    @Schema(description = "URL адрес хранения изображения", example = "")
    private String image;

    @Schema(description = "Описание", example = "")
    private String description;

    @Schema(description = "Время публикации новости", example = "")
    private LocalDateTime createdAt;
}
