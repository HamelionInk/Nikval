package com.nikitin.roadmaps.client;

import com.nikitin.roadmaps.config.security.KeycloakTokenService;
import com.nikitin.roadmaps.dto.request.ProfileRequestDto;
import com.vaadin.flow.component.upload.receivers.FileData;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Component
public class ProfileClient extends Client {

    public ProfileClient(RestTemplate restTemplate, KeycloakTokenService keycloakTokenService) {
        super(restTemplate, keycloakTokenService);
    }

    public ResponseEntity<String> create(ProfileRequestDto profileRequestDto, Boolean notificationError) {
        return request("/profiles",
                HttpMethod.POST,
                buildRequestBody(profileRequestDto, null),
                Collections.EMPTY_MAP,
                notificationError
        );
    }

    public ResponseEntity<String> uploadAvatar(Long id, FileData image, Boolean notificationError) {
            MultiValueMap<String, String> customHeader = new LinkedMultiValueMap<>();
            customHeader.add("Content-Type", ContentType.MULTIPART_FORM_DATA.getMimeType());

            MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
            multipartBodyBuilder.part("image", new FileSystemResource(image.getFile()), MediaType.valueOf(image.getMimeType()));

            return request("/profiles/upload/{id}",
                    HttpMethod.POST,
                    buildRequestBody(multipartBodyBuilder.build(), customHeader),
                    Collections.singletonMap("id", id),
                    notificationError);
    }

    public ResponseEntity<String> patch(Long id, ProfileRequestDto profileRequestDto, Boolean notificationError) {
        return request("/profiles/{id}",
                HttpMethod.PATCH,
                buildRequestBody(profileRequestDto, null),
                Collections.singletonMap("id", id),
                notificationError
        );
    }

    public ResponseEntity<String> getById(Long id, Boolean notificationError) {
        return request("/profiles/{id}",
                HttpMethod.GET,
                buildRequestBody(null, null),
                Collections.singletonMap("id", id),
                notificationError);
    }

    public ResponseEntity<String> getByEmail(String email, Boolean notificationError) {
        return request("/profiles/email/{email}",
                HttpMethod.GET,
                buildRequestBody(null, null),
                Collections.singletonMap("email", email),
                notificationError
        );
    }
}
