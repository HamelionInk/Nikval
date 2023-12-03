package com.nikitin.roadmapfrontend.dto.enums;

public enum VaadinSessionAttribute {

    REFRESH_TOKEN("refresh_token"),
    ACCESS_TOKEN("access_token"),
    EXPIRED_TOKEN("expired_token"),
    PREVIOUS_URL("previous_url");


    private final String value;

    VaadinSessionAttribute(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
