package com.nikitin.roadmaps.client;

import com.nikitin.roadmaps.config.security.KeycloakTokenService;
import com.nikitin.roadmaps.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.dto.response.ProfileResponseDto;
import com.nikitin.roadmaps.exception.TokenExpiredException;
import com.vaadin.flow.component.upload.receivers.FileData;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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
                notificationError
        );
    }

    public ResponseEntity<String> uploadAvatar(Long id, FileData image, Boolean notificationError) {
        try {
            MultiValueMap<String, String> customHeader = new LinkedMultiValueMap<>();
            customHeader.add("Content-Type", ContentType.MULTIPART_FORM_DATA.getMimeType());

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", image.getFile());

            return request("/profiles/upload/" + id,
                    HttpMethod.POST,
                    buildRequestBody(body, customHeader),
                    notificationError);
        } catch (Exception exception) {
            throw new TokenExpiredException(exception.getMessage());
        }
    }

    public ResponseEntity<String> patch(Long id, ProfileRequestDto profileRequestDto, Boolean notificationError) {
        return request("/profiles/" + id,
                HttpMethod.PATCH,
                buildRequestBody(profileRequestDto, null),
                notificationError
        );
    }

    public ResponseEntity<String> getByEmail(String email, Boolean notificationError) {
        return request("/profiles/email/" + email,
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError
        );
    }
}
