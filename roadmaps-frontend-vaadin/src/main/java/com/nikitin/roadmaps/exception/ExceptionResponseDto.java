package com.nikitin.roadmaps.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponseDto {

    private String status;
    private String message;
    private String path;

}
