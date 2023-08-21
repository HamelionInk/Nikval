package com.nikitin.roadmaps.roadmapsbackendspring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDto {

    private Long id;
    private byte picture;
    private String name;
    private String lastName;
    private String email;
    private String competence;
    private String speciality;
}
