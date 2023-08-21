package com.nikitin.roadmaps.roadmapsbackendspring.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BadRequest.class)
    public ResponseEntity<ExceptionResponseDto> handleBadRequestException(Exception exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status.value())
                .body(ExceptionResponseDto.builder()
                        .status(String.valueOf(status.value()))
                        .message(exception.getMessage())
                        .path(request.getContextPath())
                        .build()
                );
    }
}
