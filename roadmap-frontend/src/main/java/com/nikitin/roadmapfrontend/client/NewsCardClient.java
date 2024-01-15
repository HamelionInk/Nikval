package com.nikitin.roadmapfrontend.client;

import com.nikitin.roadmapfrontend.configuration.rest.RestTemplateService;
import com.nikitin.roadmapfrontend.dto.request.NewsCardRequestDto;
import com.nikitin.roadmapfrontend.dto.response.NewsCardResponseDto;
import com.nikitin.roadmapfrontend.dto.response.pageable.PageableNewsCardResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsCardClient {

    private final RestTemplateService restTemplateService;

    public NewsCardResponseDto create(NewsCardRequestDto newsCardRequestDto) {
        return restTemplateService.request("/news-card",
                HttpMethod.POST,
                restTemplateService.buildRequestBody(newsCardRequestDto, null),
                Collections.EMPTY_MAP,
                NewsCardResponseDto.class
        );
    }

    public NewsCardResponseDto patch(Long id, NewsCardRequestDto newsCardRequestDto) {
        return restTemplateService.request("/news-card/" + id,
                HttpMethod.PATCH,
                restTemplateService.buildRequestBody(newsCardRequestDto, null),
                Collections.EMPTY_MAP,
                NewsCardResponseDto.class
        );
    }

    public PageableNewsCardResponseDto getAll() {
        return restTemplateService.request("/news-card",
                HttpMethod.GET,
                restTemplateService.buildRequestBody(null, null),
                Collections.EMPTY_MAP,
                PageableNewsCardResponseDto.class
        );
    }

    public NewsCardResponseDto getById(Long id) {
        return restTemplateService.request("/news-card/" + id,
                HttpMethod.GET,
                restTemplateService.buildRequestBody(null, null),
                Collections.EMPTY_MAP,
                NewsCardResponseDto.class
        );
    }

    public void deleteById(Long id) {
        restTemplateService.request("/news-card/" + id,
                HttpMethod.DELETE,
                restTemplateService.buildRequestBody(null, null),
                Collections.EMPTY_MAP,
                String.class
        );
    }
}
