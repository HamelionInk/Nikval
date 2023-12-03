package com.nikitin.roadmapfrontend.configuration.security;

import com.nikitin.roadmapfrontend.dto.enums.VaadinSessionAttribute;
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakTokenService {

    @Value("${spring.security.oauth2.resourceserver.jwt.refresh_token_uri}")
    private String REFRESH_TOKEN_URI;
    private static final String GRANT_TYPE = "refresh_token";

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String KC_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String KC_CLIENT_SECRET;

    public String getAccessToken() {
        return (String) VaadinSession.getCurrent().getAttribute(VaadinSessionAttribute.ACCESS_TOKEN.getValue());
    }
}
