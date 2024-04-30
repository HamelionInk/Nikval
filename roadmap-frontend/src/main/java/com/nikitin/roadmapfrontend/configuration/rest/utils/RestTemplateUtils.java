package com.nikitin.roadmapfrontend.configuration.rest.utils;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class RestTemplateUtils {

	public Set<Map.Entry<String, Object>> entrySetWithNonNullValue(Map<String, Object> params) {
		return params.entrySet()
				.stream()
				.filter(entry -> Objects.nonNull(entry.getValue()))
				.collect(Collectors.toSet());
	}

	public String converterUriValue(List<?> values) {
		return values.stream()
				.map(Object::toString)
				.collect(Collectors.joining(","));
	}
}
