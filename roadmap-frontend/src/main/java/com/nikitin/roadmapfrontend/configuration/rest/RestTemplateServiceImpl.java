package com.nikitin.roadmapfrontend.configuration.rest;

import com.nikitin.roadmapfrontend.configuration.rest.utils.RestTemplateUtils;
import com.nikitin.roadmapfrontend.configuration.security.KeycloakTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestTemplateServiceImpl implements RestTemplateService {

	private final RestTemplate restTemplate;
	private final KeycloakTokenService keycloakTokenService;

	@Override
	public <T> T request(String url, HttpMethod httpMethod, HttpEntity<?> request, Map<String, Object> uriParams, Class<T> classType) {
		return restTemplate.exchange(
				buildRequestUrl(url, uriParams),
				httpMethod,
				request,
				classType,
				buildRequestParam(uriParams)
		).getBody();
	}

	@Override
	public <T> HttpEntity<T> buildRequestBody(T requestDto, MultiValueMap<String, String> customHeader) {
		return new HttpEntity<>(requestDto, buildMultiValueForHeader(customHeader));
	}

	@Override
	public MultiValueMap<String, String> buildMultiValueForHeader(MultiValueMap<String, String> customHeader) {
		if (Objects.isNull(customHeader)) {
			MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
			header.add("Authorization", "Bearer " + keycloakTokenService.getAccessToken());

			return header;
		}
		customHeader.add("Authorization", "Bearer " + keycloakTokenService.getAccessToken());

		return customHeader;
	}

	@Override
	public Map<String, Object> buildRequestParam(Map<String, Object> uriParams) {
		var params = new HashMap<String, Object>();

		Optional.ofNullable(uriParams)
				.ifPresent(requestParams ->
						RestTemplateUtils.entrySetWithNonNullValue(uriParams)
								.forEach(entry -> {
									if (entry.getValue() instanceof List<?> values) {
										params.put(entry.getKey(), RestTemplateUtils.converterUriValue(values));
									} else {
										params.put(entry.getKey(), entry.getValue());
									}
								})
				);

		return params;
	}

	@Override
	public String buildRequestUrl(String url, Map<String, Object> uriParams) {
		var uriComponentsBuilder = UriComponentsBuilder.fromUriString(url);
		Optional.ofNullable(uriParams)
				.ifPresent(requestParams ->
						RestTemplateUtils.entrySetWithNonNullValue(uriParams)
								.forEach(entry ->
										uriComponentsBuilder.queryParam(entry.getKey(), "{" + entry.getKey() + "}")
								)
				);

		return uriComponentsBuilder.encode().toUriString();
	}
}
