package com.nikitin.roadmaps.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponseDto {

    private UUID id;

    private String username;

    private String password;

    private String email;
}
