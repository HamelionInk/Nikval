package com.nikitin.roadmapfrontend.configuration.vaadin;

import com.nikitin.roadmapfrontend.client.ProfileClient;
import com.nikitin.roadmapfrontend.utils.enums.VaadinSessionAttribute;
import com.nikitin.roadmapfrontend.dto.request.ProfileRequestDto;
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

import java.util.Objects;
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

                    OAuth2AuthenticationToken currentUser = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                    OAuth2AuthorizedClient authorizedClient = this.oAuth2AuthorizedClientService
                            .loadAuthorizedClient(currentUser.getAuthorizedClientRegistrationId(), currentUser.getName());

                    Optional.ofNullable(authorizedClient.getRefreshToken())
                            .ifPresent(refreshToken ->
                                    initEvent.getSession().setAttribute(VaadinSessionAttribute.REFRESH_TOKEN.getValue(), refreshToken.getTokenValue())
                            );

                    Optional.ofNullable(authorizedClient.getAccessToken())
                            .ifPresent(accessToken -> {
                                initEvent.getSession().setAttribute(VaadinSessionAttribute.ACCESS_TOKEN.getValue(), accessToken.getTokenValue());
                                initEvent.getSession().setAttribute(VaadinSessionAttribute.EXPIRED_TOKEN.getValue(), accessToken.getExpiresAt());
                            });

                    createOrUpdateProfile((OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                }
        );
    }

    private void createOrUpdateProfile(OidcUser userInfo) {
        var response = profileClient.getByEmail(userInfo.getEmail());

        if (Objects.nonNull(response.getId())) {
            profileClient.patch(response.getId(), ProfileRequestDto.builder()
                    .lastDateLogin(userInfo.getAuthenticatedAt())
                    .build());
            VaadinSession.getCurrent().setAttribute(VaadinSessionAttribute.PROFILE_ID.getValue(), response.getId());
        } else {
            var response2 = profileClient.create(ProfileRequestDto.builder()
                    .name(userInfo.getGivenName())
                    .lastName(userInfo.getFamilyName())
                    .email(userInfo.getEmail())
                    .lastDateLogin(userInfo.getAuthenticatedAt())
                    .build());

            if (Objects.nonNull(response2)) {
                VaadinSession.getCurrent().setAttribute(VaadinSessionAttribute.PROFILE_ID.getValue(), response2.getId());
            }
        }
    }
}
