package com.nikitin.roadmaps.client;

import com.nikitin.roadmaps.config.security.KeycloakTokenService;
import com.nikitin.roadmaps.exception.ExceptionResponseDto;
import com.nikitin.roadmaps.util.RestUtils;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class Client {

    private final RestTemplate restTemplate;
    private final KeycloakTokenService keycloakTokenService;

    public ResponseEntity<String> request(String url, HttpMethod httpMethod, HttpEntity<?> request, Boolean notificationError) {
        if (notificationError) {
            return requestWithNotificationError(url, httpMethod, request);
        }
        return requestWithoutNotificationError(url, httpMethod, request);
    }

    public <T> HttpEntity<T> buildRequestBody(T requestDto, MultiValueMap<String, String> customHeader) {
        return new HttpEntity<>(requestDto, buildMultiValueForHeader(customHeader));
    }

    public RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    private ResponseEntity<String> requestWithNotificationError(String url, HttpMethod httpMethod, HttpEntity<?> request) {
        try {
            return restTemplate.exchange(url, httpMethod, request, String.class);
        } catch (HttpStatusCodeException exception) {
            if(!exception.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                var exceptionResponseBody = RestUtils.convertResponseToDto(exception.getResponseBodyAsString(), ExceptionResponseDto.class);
                Notification errorNotification = Notification.show(exceptionResponseBody.getStatus() + " - " + exceptionResponseBody.getMessage());
                errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                errorNotification.setPosition(Notification.Position.BOTTOM_END);
            }
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getResponseBodyAsString());
        }
    }

    private ResponseEntity<String> requestWithoutNotificationError(String url, HttpMethod httpMethod, HttpEntity<?> request) {
        try {
            return restTemplate.exchange(url, httpMethod, request, String.class);
        } catch (HttpStatusCodeException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getResponseBodyAsString());
        }
    }

    private MultiValueMap<String, String> buildMultiValueForHeader(MultiValueMap<String, String> customHeader) {
        if(Objects.isNull(customHeader)) {
            MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
            header.add("Authorization", "Bearer " + keycloakTokenService.getAccessToken());

            return header;
        }
        customHeader.add("Authorization", "Bearer " + keycloakTokenService.getAccessToken());

        return customHeader;
    }
}
