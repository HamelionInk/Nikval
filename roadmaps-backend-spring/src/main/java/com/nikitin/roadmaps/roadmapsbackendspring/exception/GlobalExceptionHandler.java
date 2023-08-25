package com.nikitin.roadmaps.roadmapsbackendspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ExceptionResponseDto> handleBadRequestException(Exception exception, WebRequest request) {
        var httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus.value())
                .body(ExceptionResponseDto.builder()
                        .status(String.valueOf(httpStatus.value()))
                        .message(exception.getMessage())
                        .path(request.getContextPath())
                        .build()
                );
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleNotFoundException(Exception exception, WebRequest request) {
        var httpStatus = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(httpStatus.value())
                .body(ExceptionResponseDto.builder()
                        .status(String.valueOf(httpStatus.value()))
                        .message(exception.getMessage())
                        .path(request.getContextPath())
                        .build()
                );
    }
}
