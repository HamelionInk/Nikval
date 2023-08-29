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

    public ResponseEntity<String> create(ProfileRequestDto profileRequestDto) {
        return request("/profiles",
                HttpMethod.POST,
                buildRequestBody(profileRequestDto)
        );
    }

    public ResponseEntity<String> patch(Long id, ProfileRequestDto profileRequestDto) {
        return request("/profiles/" + id,
                HttpMethod.PATCH,
                buildRequestBody(profileRequestDto)
        );
    }

    public ResponseEntity<String> getByEmail(String email) {
        return request("/profiles/email/" + email,
                HttpMethod.GET,
                buildRequestBody(null)
        );
    }
}
