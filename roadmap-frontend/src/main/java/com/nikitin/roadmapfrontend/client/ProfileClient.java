package com.nikitin.roadmapfrontend.client;

import com.nikitin.roadmapfrontend.configuration.rest.RestTemplateService;
import com.nikitin.roadmapfrontend.dto.request.ProfileRequestDto;
import com.nikitin.roadmapfrontend.dto.response.ProfileResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileClient {

    private final RestTemplateService restTemplateService;

    public ProfileResponseDto create(ProfileRequestDto profileRequestDto) {
        return restTemplateService.request("/profiles",
                HttpMethod.POST,
                restTemplateService.buildRequestBody(profileRequestDto, null),
                Collections.EMPTY_MAP,
                ProfileResponseDto.class
        );
    }

//    public ResponseEntity<String> uploadAvatar(Long id, FileData image, Boolean notificationError) {
//            MultiValueMap<String, String> customHeader = new LinkedMultiValueMap<>();
//            customHeader.add("Content-Type", ContentType.MULTIPART_FORM_DATA.getMimeType());
//
//            MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
//            multipartBodyBuilder.part("image", new FileSystemResource(image.getFile()), MediaType.valueOf(image.getMimeType()));
//
//            return request("/profiles/upload/{id}",
//                    HttpMethod.POST,
//                    buildRequestBody(multipartBodyBuilder.build(), customHeader),
//                    Collections.singletonMap("id", id),
//                    notificationError);
//    }

    public ProfileResponseDto patch(Long id, ProfileRequestDto profileRequestDto) {
        return restTemplateService.request("/profiles/{id}",
                HttpMethod.PATCH,
                restTemplateService.buildRequestBody(profileRequestDto, null),
                Collections.singletonMap("id", id),
                ProfileResponseDto.class
        );
    }

    public ProfileResponseDto getById(Long id) {
        return restTemplateService.request("/profiles/{id}",
                HttpMethod.GET,
                restTemplateService.buildRequestBody(null, null),
                Collections.singletonMap("id", id),
                ProfileResponseDto.class);
    }

    public ProfileResponseDto getByEmail(String email) {
        return restTemplateService.request("/profiles/email/{email}",
                HttpMethod.GET,
                restTemplateService.buildRequestBody(null, null),
                Collections.singletonMap("email", email),
                ProfileResponseDto.class
        );
    }
}
