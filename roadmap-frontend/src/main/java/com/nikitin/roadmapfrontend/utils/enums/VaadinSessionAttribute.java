package com.nikitin.roadmapfrontend.utils.enums;

import lombok.Getter;

@Getter
public enum VaadinSessionAttribute {

    REFRESH_TOKEN("refresh_token"),
    ACCESS_TOKEN("access_token"),
    EXPIRED_TOKEN("expired_token"),
    PREVIOUS_URL("previous_url"),
    PROFILE_ID("profile_id");

    private final String value;

    VaadinSessionAttribute(String value) {
        this.value = value;
    }

}
