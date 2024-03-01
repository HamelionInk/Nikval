package com.nikitin.roadmapfrontend.view;

import org.springframework.util.StringUtils;

public interface View {

    void refreshView();

    <T> T getClient(Class<T> clientType);

    default String hasValue(String value) {
        if (StringUtils.hasText(value)) {
            return value;
        }
        return null;
    }
}
