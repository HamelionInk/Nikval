package com.nikitin.roadmaps.roadmapsbackendspring.dto.request;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.enums.CompetenceType;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Create;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Patch;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.annotation.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequestDto {

    @NotBlank(message = "Поле <picture> должно содержать значение", allowNull = true, groups = {Create.class, Patch.class})
    private String picture;

    @NotNull(message = "Поле <name> не может быть null", groups = Create.class)
    @NotBlank(message = "Поле <name> должно содержать значение", allowNull = true, groups = {Create.class, Patch.class})
    private String name;

    @NotNull(message = "Поле <lastName> не может быть null", groups = Create.class)
    @NotBlank(message = "Поле <lastName> должно содержать значение", allowNull = true, groups = {Create.class, Patch.class})
    private String lastName;

    @NotNull(message = "Поле <email> не может быть null", groups = Create.class)
    @NotBlank(message = "Поле <email> должно содержать значение", allowNull = true, groups = {Create.class, Patch.class})
    @Email(message = "Неверный формат почты", groups = {Create.class, Patch.class})
    private String email;

    private CompetenceType competence;

    @NotBlank(message = "Поле <speciality> должно содержать значение", allowNull = true, groups = {Create.class, Patch.class})
    private String speciality;

    @NotNull(message = "Поле <lastDateLogin> не может быть null", groups = Create.class)
    private Instant lastDateLogin;
}
