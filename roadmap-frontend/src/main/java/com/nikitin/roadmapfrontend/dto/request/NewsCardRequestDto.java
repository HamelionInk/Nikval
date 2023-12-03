package com.nikitin.roadmapfrontend.dto.request;

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
public class NewsCardRequestDto {

    private String title;
    private String image;
    private String description;
    private LocalDateTime createdAt;
}
