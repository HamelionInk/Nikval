package com.nikitin.roadmaps.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.nikitin.roadmaps.exception.JsonReadValueException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class RestUtils {

    public <T> T convertResponseToDto(String response, Class<T> dto) {
        try {
            ObjectMapper jsonMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
            return jsonMapper.readValue(response, dto);
        } catch (Exception exception) {
            throw new JsonReadValueException(exception.getMessage());
        }
    }
}
