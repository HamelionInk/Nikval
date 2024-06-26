package com.nikitin.roadmapfrontend.dto.response;

import com.nikitin.roadmapfrontend.utils.enums.CompetenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDto {

    private Long id;
    private String picture;
    private String name;
    private String lastName;
    private String fullName;
    private String email;
    private CompetenceType competence;
    private String speciality;
    private Instant lastDateLogin;
    private Instant birthDate;
    private String residentialAddress;
}
