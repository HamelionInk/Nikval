package com.nikitin.roadmapfrontend.configuration.rest;

import com.nikitin.roadmapfrontend.configuration.security.KeycloakLogoutHandler;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinServletRequest;
import jakarta.servlet.ServletException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestTemplateErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(@NonNull ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(@NonNull ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            try {
                Notification.show(response.getStatusCode().toString());

                VaadinServletRequest.getCurrent().logout();
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }

        if(response.getStatusCode().is4xxClientError()) {
            log.info(response.getStatusText() + " " + response.getStatusCode());
        }
    }
}
