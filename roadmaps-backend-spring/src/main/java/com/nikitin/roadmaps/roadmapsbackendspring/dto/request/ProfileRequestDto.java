package com.nikitin.roadmaps.roadmapsbackendspring.dto.request;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.enums.CompetenceType;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Create;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Patch;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.annotation.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO запроса Profile")
public class ProfileRequestDto {

    @Schema(description = "URL адрес хранения изображения", example = "https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8cHJvZmlsZXxlbnwwfHwwfHx8MA%3D%3D&w=1000&q=80", nullable = true)
    @NotBlank(message = "Поле <picture> должно содержать значение", allowNull = true, groups = {Create.class, Patch.class})
    private String picture;

    @Schema(description = "Имя пользователя", example = "Michael")
    @NotNull(message = "Поле <name> не может быть null", groups = Create.class)
    @NotBlank(message = "Поле <name> должно содержать значение", allowNull = true, groups = {Create.class, Patch.class})
    private String name;

    @Schema(description = "Фамилия пользователя", example = "Nikitin")
    @NotNull(message = "Поле <lastName> не может быть null", groups = Create.class)
    @NotBlank(message = "Поле <lastName> должно содержать значение", allowNull = true, groups = {Create.class, Patch.class})
    private String lastName;

    @Schema(description = "Электронная почта пользователя", example = "myemail@gmail.com")
    @NotNull(message = "Поле <email> не может быть null", groups = Create.class)
    @NotBlank(message = "Поле <email> должно содержать значение", allowNull = true, groups = {Create.class, Patch.class})
    @Email(message = "Неверный формат почты", groups = {Create.class, Patch.class})
    private String email;

    @Schema(description = "Компетенция пользователя", example = "JUNIOR", nullable = true)
    private CompetenceType competence;

    @Schema(description = "Специализация пользователя", example = "Java backend", nullable = true)
    @NotBlank(message = "Поле <speciality> должно содержать значение", allowNull = true, groups = {Create.class, Patch.class})
    private String speciality;

    @Schema(description = "Дата последнего входа в аккаунт пользователя", example = "2023-08-28T09:18:55.766Z")
    @NotNull(message = "Поле <lastDateLogin> не может быть null", groups = Create.class)
    private Instant lastDateLogin;

    @Schema(description = "Дата рождения пользователя", example = "1996-08-28T09:18:55.766Z")
    @PastOrPresent(message = "Поле <birthDate> не может быть больше настоящего времени")
    private Instant birthDate;

    @Schema(description = "Место проживания пользователя", example = "г.Пенза, Московская 21, кв 5")
    @NotBlank(message = "Поле <residentialAddress> должно содержать значение", allowNull = true, groups = {Create.class, Patch.class})
    private String residentialAddress;
}
