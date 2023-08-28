package com.nikitin.roadmaps.roadmapsbackendspring.dto.response;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.enums.CompetenceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO ответа Profile")
public class ProfileResponseDto {

    @Schema(description = "Идентификатор в базе данных", example = "4")
    private Long id;

    @Schema(description = "URL адрес хранения изображения", example = "https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8cHJvZmlsZXxlbnwwfHwwfHx8MA%3D%3D&w=1000&q=80")
    private String picture;

    @Schema(description = "Имя пользователя", example = "Michael")
    private String name;

    @Schema(description = "Фамилия пользователя", example = "Nikitin")
    private String lastName;

    @Schema(description = "Полное имя пользователя", example = "Michael Nikitin")
    private String fullName;

    @Schema(description = "Электронная почта пользователя", example = "myemail@gmail.com")
    private String email;

    @Schema(description = "Компетенция пользователя", example = "JUNIOR")
    private CompetenceType competence;

    @Schema(description = "Специализация пользователя", example = "Java backend")
    private String speciality;

    @Schema(description = "Дата последнего входа в аккаунт пользователя", example = "2023-08-28T09:18:55.766Z")
    private Instant lastDateLogin;
}
