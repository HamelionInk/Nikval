package com.nikitin.roadmapfrontend.dto.response.pageable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nikitin.roadmapfrontend.dto.response.NewsCardResponseDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageableNewsCardResponseDto extends Pageable {

    @JsonProperty("content")
    @Builder.Default
    private List<NewsCardResponseDto> newsCardResponseDtos = new ArrayList<>();
}
