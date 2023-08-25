package com.nikitin.roadmaps.dto.response;

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
    private String competence;
    private String speciality;
    private Instant lastDateLogin;
}
