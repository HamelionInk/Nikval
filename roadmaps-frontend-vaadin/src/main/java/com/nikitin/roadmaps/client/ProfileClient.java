package com.nikitin.roadmaps.client;

import com.nikitin.roadmaps.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.dto.response.ProfileResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ProfileClient extends Client {

    @Autowired
    public ProfileClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public void createProfileForFirstAuthentication(OidcUser userInfo) {
        try {
            request("/profiles",
                    HttpMethod.POST,
                    buildRequestBody(ProfileRequestDto.builder()
                            .picture(userInfo.getPicture())
                            .name(userInfo.getGivenName())
                            .lastName(userInfo.getFamilyName())
                            .email(userInfo.getEmail())
                            .build()),
                    ProfileResponseDto.class
            );
        } catch (Exception exception) {
            //do nothing
        }
    }

    public ProfileResponseDto getProfileByEmail(String email) {
        return request("/profiles/" + email,
                HttpMethod.GET,
                null,
                ProfileResponseDto.class
        );
    }
}
