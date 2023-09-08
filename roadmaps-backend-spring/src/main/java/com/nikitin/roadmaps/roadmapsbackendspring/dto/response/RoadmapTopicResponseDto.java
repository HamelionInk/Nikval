package com.nikitin.roadmaps.roadmapsbackendspring.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "")
public class RoadmapTopicResponseDto {

    @Schema(description = "Идентификатор в базе данных", example = "32")
    private Long id;

    @Schema(description = "Название темы", example = "Stream API")
    private String name;

    @Schema(description = "Количество вопросов в теме", example = "43")
    private Integer numberOfQuestion;

    @Schema(description = "Количество изученных вопросов в теме", example = "12")
    private Integer numberExploredQuestion;

    @Schema(description = "Идентификатор отношения к разделу", example = "8")
    private Long roadmapChapterId;

    @Schema(description = "", example = "")
    private List<RoadmapQuestionResponseDto> roadmapQuestionResponseDtos;
}
