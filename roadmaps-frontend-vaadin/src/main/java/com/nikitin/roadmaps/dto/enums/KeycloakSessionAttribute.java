package com.nikitin.roadmaps.dto.enums;

public enum KeycloakSessionAttribute {

    REFRESH_TOKEN("refresh_token"),
    ACCESS_TOKEN("access_token"),
    EXPIRED_TOKEN("expired_token");

    private final String value;

    KeycloakSessionAttribute(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
