package com.nikitin.roadmapfrontend.configuration.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface RestTemplateService {

    <T> T request(String url, HttpMethod httpMethod, HttpEntity<?> request, Map<String, Object> uriParams, Class<T> classType);

    <T> HttpEntity<T> buildRequestBody(T requestDto, MultiValueMap<String, String> customHeader);

    MultiValueMap<String, String> buildMultiValueForHeader(MultiValueMap<String, String> customHeader);

    Map<String, Object> buildRequestParam(Map<String, Object> uriParams);

    String buildRequestUrl(String url, Map<String, Object> uriParams);

}
