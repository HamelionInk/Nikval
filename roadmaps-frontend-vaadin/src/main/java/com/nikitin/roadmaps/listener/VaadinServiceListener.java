package com.nikitin.roadmaps.listener;

import com.nikitin.roadmaps.client.ProfileClient;
import com.nikitin.roadmaps.dto.enums.KeycloakSessionAttribute;
import com.nikitin.roadmaps.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.dto.response.ProfileResponseDto;
import com.nikitin.roadmaps.exception.VaadinExceptionHandler;
import com.nikitin.roadmaps.util.RestUtils;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class VaadinServiceListener implements VaadinServiceInitListener {

    private final ProfileClient profileClient;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        serviceInitEvent.getSource().addSessionInitListener(
                initEvent -> {
                    initEvent.getSession().setErrorHandler(new VaadinExceptionHandler());

                    OAuth2AuthenticationToken currentUser = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                    OAuth2AuthorizedClient authorizedClient = this.oAuth2AuthorizedClientService
                            .loadAuthorizedClient(currentUser.getAuthorizedClientRegistrationId(), currentUser.getName());

                    Optional.ofNullable(authorizedClient.getRefreshToken())
                            .ifPresent(refreshToken ->
                                    initEvent.getSession().setAttribute(KeycloakSessionAttribute.REFRESH_TOKEN.getValue(), refreshToken.getTokenValue())
                            );

                    Optional.ofNullable(authorizedClient.getAccessToken())
                            .ifPresent(accessToken -> {
                                initEvent.getSession().setAttribute(KeycloakSessionAttribute.ACCESS_TOKEN.getValue(), accessToken.getTokenValue());
                                initEvent.getSession().setAttribute(KeycloakSessionAttribute.EXPIRED_TOKEN.getValue(), accessToken.getExpiresAt());
                            });

                    createOrUpdateProfile((OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                }
        );
    }

    private void createOrUpdateProfile(OidcUser userInfo) {
        var response = profileClient.getByEmail(userInfo.getEmail(), false);
        if (response.getStatusCode().is2xxSuccessful()) {
            Optional.ofNullable(response.getBody())
                    .ifPresent(responseBody -> {
                        var profileResponseDto = RestUtils.convertResponseToDto(responseBody, ProfileResponseDto.class);
                        profileClient.patch(profileResponseDto.getId(), ProfileRequestDto.builder()
                                .lastDateLogin(userInfo.getAuthenticatedAt())
                                .build(), false);
                        VaadinSession.getCurrent().setAttribute("profileId", profileResponseDto.getId());
                    });
        }

        if (response.getStatusCode().is4xxClientError()) {
            var responseEntity = profileClient.create(ProfileRequestDto.builder()
                    .name(userInfo.getGivenName())
                    .lastName(userInfo.getFamilyName())
                    .email(userInfo.getEmail())
                    .lastDateLogin(userInfo.getAuthenticatedAt())
                    .build(), false);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Optional.ofNullable(responseEntity.getBody())
                        .ifPresent(responseBody -> {
                            var profileResponseDto = RestUtils.convertResponseToDto(responseBody, ProfileResponseDto.class);
                            VaadinSession.getCurrent().setAttribute("profileId", profileResponseDto.getId());
                        });
            }
        }
    }
}
