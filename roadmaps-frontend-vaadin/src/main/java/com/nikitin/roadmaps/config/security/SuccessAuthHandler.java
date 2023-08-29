package com.nikitin.roadmaps.config.security;

import com.nikitin.roadmaps.client.ProfileClient;
import com.nikitin.roadmaps.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.dto.response.ProfileResponseDto;
import com.nikitin.roadmaps.util.RestUtils;
import com.vaadin.flow.spring.security.VaadinSavedRequestAwareAuthenticationSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuccessAuthHandler extends VaadinSavedRequestAwareAuthenticationSuccessHandler {

    private final ProfileClient profileClient;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        super.onAuthenticationSuccess(request, response, authentication);

        createOrUpdateProfile((OidcUser) authentication.getPrincipal());
    }

    private void createOrUpdateProfile(OidcUser userInfo) {
        var response = profileClient.getByEmail(userInfo.getEmail());
        if (response.getStatusCode().is2xxSuccessful()) {
            Optional.ofNullable(response.getBody())
                    .ifPresent(responseBody -> {
                        var profileResponseDto = RestUtils.convertResponseToDto(responseBody, ProfileResponseDto.class);
                        profileClient.patch(profileResponseDto.getId(), ProfileRequestDto.builder()
                                .lastDateLogin(userInfo.getAuthenticatedAt())
                                .build());
                    });
        }

        if (response.getStatusCode().is4xxClientError()) {
            profileClient.create(ProfileRequestDto.builder()
                    .name(userInfo.getGivenName())
                    .lastName(userInfo.getFamilyName())
                    .email(userInfo.getEmail())
                    .lastDateLogin(userInfo.getAuthenticatedAt())
                    .build());
        }
    }
}

