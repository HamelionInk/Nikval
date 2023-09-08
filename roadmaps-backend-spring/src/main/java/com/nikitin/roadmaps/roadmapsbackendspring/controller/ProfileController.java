package com.nikitin.roadmaps.roadmapsbackendspring.controller;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.ProfileResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.exception.ExceptionResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.service.ProfileService;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Create;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Patch;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
@Tag(name = "Профиль")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    @Operation(description = "Создание профиля сотрудника", method = "POST")
    @ApiResponse(responseCode = "201", description = "CREATED - Профиль успешно создан",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProfileResponseDto.class))})
    @ApiResponse(responseCode = "400", description = "BAD REQUEST - Ошибка в запросе",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ExceptionResponseDto.class))})
    public ResponseEntity<ProfileResponseDto> create(@RequestBody @Validated(value = Create.class) ProfileRequestDto profileRequestDto) {
        var responseBody = profileService.create(profileRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PostMapping(value = "/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable (name = "id") Long id,
                                               @RequestPart(name = "image") MultipartFile image) {
            var responseBody = profileService.uploadAvatar(id, image);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PatchMapping("/{id}")
    @Operation(description = "Обновление профиля сотрудника по идентификатору (Поля со значением Null не обновляются)", method = "PATCH")
    @ApiResponse(responseCode = "202", description = "ACCEPTED - Профиль успешно обновлен",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProfileResponseDto.class))})
    @ApiResponse(responseCode = "400", description = "BAD REQUEST - Ошибка в запросе",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ExceptionResponseDto.class))})
    @ApiResponse(responseCode = "404", description = "NOT FOUND - Профиль с указанным идентификатором не найден",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ExceptionResponseDto.class))})
    public ResponseEntity<ProfileResponseDto> patch(@PathVariable(name = "id") Long id,
                                                    @RequestBody @Validated(value = Patch.class) ProfileRequestDto profileRequestDto) {
        var responseBody = profileService.patch(id, profileRequestDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @GetMapping("/{id}")
    @Operation(description = "Получение профиля пользователя по уникальному идентификатору", method = "GET")
    @ApiResponse(responseCode = "202", description = "ACCEPTED - Профиль успешно получен",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProfileResponseDto.class))})
    @ApiResponse(responseCode = "404", description = "NOT FOUND - Профиль с указанным идентификатором не найден",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ExceptionResponseDto.class))})
    public ResponseEntity<ProfileResponseDto> getById(@PathVariable(name = "id") Long id) {
        var responseBody = profileService.getResponseById(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @GetMapping("/email/{email}")
    @Operation(description = "Получение профиля пользователя по электронной почте", method = "GET")
    @ApiResponse(responseCode = "202", description = "ACCEPTED - Профиль успешно получен",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProfileResponseDto.class))})
    @ApiResponse(responseCode = "404", description = "NOT FOUND - Профиль с указанной электронной почтой не найден",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ExceptionResponseDto.class))})
    public ResponseEntity<ProfileResponseDto> getByEmail(@PathVariable(name = "email") String email) {
        var responseBody = profileService.getByEmail(email);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @GetMapping
    @Operation(description = "Получение всех профилей пользователей", method = "GET")
    @ApiResponse(responseCode = "202", description = "ACCEPTED - Профили успешно получены",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProfileResponseDto.class))})
    public ResponseEntity<Page<ProfileResponseDto>> getAll(@ParameterObject @PageableDefault(sort = "name",
            direction = Sort.Direction.ASC,
            size = Integer.MAX_VALUE) Pageable pageable) {
        var responseBody = profileService.getAll(pageable);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }
}
