package com.nikitin.roadmaps.client;

import com.nikitin.roadmaps.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.dto.response.ProfileResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ProfileClient extends Client {

    @Autowired
    public ProfileClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public ResponseEntity<ProfileResponseDto> create(ProfileRequestDto profileRequestDto) {
        return request("/profiles",
                HttpMethod.POST,
                buildRequestBody(profileRequestDto),
                ProfileResponseDto.class
        );
    }

    public ResponseEntity<ProfileResponseDto> patch(Long id, ProfileRequestDto profileRequestDto) {
        return request("/profiles/" + id,
                HttpMethod.PATCH,
                buildRequestBody(profileRequestDto),
                ProfileResponseDto.class
        );
    }

    public ResponseEntity<ProfileResponseDto> getByEmail(String email) {
        return request("/profiles/email/" + email,
                HttpMethod.GET,
                null,
                ProfileResponseDto.class
        );
    }
}
