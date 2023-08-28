package com.nikitin.roadmaps.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
@UtilityClass
public class ViewUtils {

    public String hasStringValue(String value) {
        if(StringUtils.hasText(value)) {
            return value;
        }
        return null;
    }
}
