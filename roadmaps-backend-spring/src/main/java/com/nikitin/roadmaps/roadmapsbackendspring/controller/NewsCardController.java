package com.nikitin.roadmaps.roadmapsbackendspring.controller;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.NewsCardRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.NewsCardResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.ProfileResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.exception.ExceptionResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.service.NewsCardService;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Create;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/news-card")
@Tag(name = "Новостная карточка")
public class NewsCardController {

    private final NewsCardService newsCardService;

    @PostMapping
    @Operation(description = "Создание новостной карточки", method = "POST")
    @ApiResponse(responseCode = "201", description = "CREATED - Новостная карточка успешно создана",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = NewsCardResponseDto.class))})
    @ApiResponse(responseCode = "400", description = "BAD REQUEST - Ошибка в запросе",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ExceptionResponseDto.class))})
    public ResponseEntity<NewsCardResponseDto> create(@RequestBody @Validated(value = Create.class) NewsCardRequestDto newsCardRequestDto) {
        var responseBody = newsCardService.create(newsCardRequestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseBody);
    }

    @PatchMapping("/{id}")
    @Operation(description = "Обновление новостной карточки", method = "PATCH")
    @ApiResponse(responseCode = "200", description = "OK - Новостная карточка успешно обновлена",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = NewsCardResponseDto.class))})
    @ApiResponse(responseCode = "400", description = "BAD REQUEST - Ошибка в запросе",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ExceptionResponseDto.class))})
    public ResponseEntity<NewsCardResponseDto> patch(@PathVariable (name = "id") Long id,
                                                     @RequestBody @Validated(value = Create.class) NewsCardRequestDto newsCardRequestDto) {
        var responseBody = newsCardService.patch(id, newsCardRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseBody);
    }

    @Operation(description = "Получить новостную карточку по идентификатору", method = "GET")
    @ApiResponse(responseCode = "200", description = "OK - Карточка получена",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = NewsCardResponseDto.class))})
    @ApiResponse(responseCode = "404", description = "NOT FOUND - Не найдено",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ExceptionResponseDto.class))})
    @GetMapping("/{id}")
    public ResponseEntity<NewsCardResponseDto> getById(@PathVariable(name = "id") Long id) {
        var responseBody = newsCardService.getResponseById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseBody);
    }

    @GetMapping
    @Operation(description = "Получение всех новостных карточек", method = "GET")
    @ApiResponse(responseCode = "200", description = "OK - Новостные карточки успешно получены",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = NewsCardResponseDto.class))})
    public ResponseEntity<Page<NewsCardResponseDto>> getAll(@ParameterObject @PageableDefault(sort = "id",
            direction = Sort.Direction.ASC,
            size = Integer.MAX_VALUE) Pageable pageable) {
        var responseBody = newsCardService.getAll(pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseBody);
    }

    @Operation(description = "Удаление новостной карточки", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "OK - Новостная карточка удалена",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = NewsCardResponseDto.class))})
    @ApiResponse(responseCode = "404", description = "NOT FOUND - Не найдено",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ExceptionResponseDto.class))})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") Long id) {
        newsCardService.deleteById(id);

        return ResponseEntity
                .status(HttpStatus.OK).build();
    }
}
