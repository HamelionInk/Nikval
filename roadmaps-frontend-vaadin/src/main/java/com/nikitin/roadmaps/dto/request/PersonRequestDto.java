package com.nikitin.roadmaps.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequestDto {

    @NotBlank(message = "Поле username не может быть пустым")
    private String username;

    @NotBlank(message = "Поле password не может быть пустым")
    private String password;

    @NotBlank(message = "Поле confirmPassword не может быть пустым")
    private String confirmPassword;

    @NotBlank(message = "Поле email не может быть пустым")
    @Email(message = "Поле email имеет некоректнные данные")
    private String email;
}
