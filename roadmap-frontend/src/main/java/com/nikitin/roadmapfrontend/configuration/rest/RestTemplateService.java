package com.nikitin.roadmapfrontend.configuration.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface RestTemplateService {

    <T> T request(String url, HttpMethod httpMethod, HttpEntity<?> request, Map<String, ?> uriParams, Class<T> classType);

    <T> HttpEntity<T> buildRequestBody(T requestDto, MultiValueMap<String, String> customHeader);

    MultiValueMap<String, String> buildMultiValueForHeader(MultiValueMap<String, String> customHeader);

}
