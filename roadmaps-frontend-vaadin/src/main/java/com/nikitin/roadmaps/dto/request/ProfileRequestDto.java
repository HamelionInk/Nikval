package com.nikitin.roadmaps.dto.request;

import com.nikitin.roadmaps.dto.enums.CompetenceType;
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

    private String picture;
    private String name;
    private String lastName;
    private String email;
    private CompetenceType competence;
    private String speciality;
    private Instant lastDateLogin;
}
