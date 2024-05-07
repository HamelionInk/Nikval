package com.nikitin.roadmapfrontend.utils.enums;

import lombok.Getter;

@Getter
public enum HttpSessionAttribute {

	SPRING_SECURITY_SAVED_REQUEST("SPRING_SECURITY_SAVED_REQUEST");

	private final String value;

	HttpSessionAttribute(String value) {
		this.value = value;
	}

}
