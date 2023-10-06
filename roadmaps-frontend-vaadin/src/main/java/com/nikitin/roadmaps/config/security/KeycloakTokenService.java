package com.nikitin.roadmaps.config.security;

import com.nikitin.roadmaps.dto.enums.KeycloakSessionAttribute;
import com.nikitin.roadmaps.dto.response.KeycloakTokenResponseDto;
import com.nikitin.roadmaps.exception.TokenExpiredException;
import com.nikitin.roadmaps.util.RestUtils;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Slf4j
@Component
public class KeycloakTokenService {

    private static final String URL_REFRESH_TOKEN = "https://keycloak.roadmaps-nikval.ru/realms/roadmaps/protocol/openid-connect/token";
    private static final String GRANT_TYPE = "refresh_token";

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String KC_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String KC_CLIENT_SECRET;

    private final RestTemplate restTemplate;

    public KeycloakTokenService() {
        restTemplate = new RestTemplate();
    }

    public String getAccessToken() {
        try {
            validateAccessToken();
            return (String) VaadinSession.getCurrent().getAttribute(KeycloakSessionAttribute.ACCESS_TOKEN.getValue());
        } catch (TokenExpiredException exception) {
            logout();
            //todo - переделать
            return "";
        }
    }

    public void validateAccessToken() throws TokenExpiredException {
        var dateExpired = (Instant) VaadinSession.getCurrent().getAttribute(KeycloakSessionAttribute.EXPIRED_TOKEN.getValue());

        if (dateExpired.getEpochSecond() <= Instant.now().getEpochSecond()) {
            refreshToken((String) VaadinSession.getCurrent().getAttribute(KeycloakSessionAttribute.REFRESH_TOKEN.getValue()));
        }
    }

    public void refreshToken(String refreshToken) {
        try {
            var response = restTemplate.exchange(
                    URL_REFRESH_TOKEN,
                    HttpMethod.POST,
                    buildHttpEntityForRefreshToken(refreshToken),
                    String.class);

            updateTokenInSession(RestUtils.convertResponseToDto(response.getBody(), KeycloakTokenResponseDto.class));
        } catch (Exception exception) {
            throw new TokenExpiredException(exception.getMessage());
        }
    }

    private void logout() {
        try {
            VaadinServletRequest.getCurrent().logout();
        } catch (ServletException e) {
            log.error("Logout failed");
        }
    }

    private void updateTokenInSession(KeycloakTokenResponseDto keycloakTokenResponseDto) {
        VaadinSession.getCurrent().setAttribute(KeycloakSessionAttribute.ACCESS_TOKEN.getValue(), keycloakTokenResponseDto.getAccessToken());
        VaadinSession.getCurrent().setAttribute(KeycloakSessionAttribute.REFRESH_TOKEN.getValue(), keycloakTokenResponseDto.getRefreshToken());
        VaadinSession.getCurrent().setAttribute(KeycloakSessionAttribute.EXPIRED_TOKEN.getValue(),
                Instant.now().plusSeconds(keycloakTokenResponseDto.getExpiresIn()));
    }

    private MultiValueMap<String, String> buildParametersForRefreshToken(String refreshToken) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", GRANT_TYPE);
        parameters.add("client_id", KC_CLIENT_ID);
        parameters.add("refresh_token", refreshToken);
        parameters.add("client_secret", KC_CLIENT_SECRET);

        return parameters;
    }

    private HttpHeaders buildHeaderForRefreshToken() {
        var httpHeader = new HttpHeaders();
        httpHeader.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return httpHeader;
    }

    private HttpEntity<Object> buildHttpEntityForRefreshToken(String refreshToken) {
        return new HttpEntity<>(buildParametersForRefreshToken(refreshToken), buildHeaderForRefreshToken());
    }
}
