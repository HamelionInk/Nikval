package com.nikitin.roadmapfrontend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsCardResponseDto {

    private Long id;
    private String title;
    private String image;
    private String description;
    private LocalDateTime createdAt;
}
