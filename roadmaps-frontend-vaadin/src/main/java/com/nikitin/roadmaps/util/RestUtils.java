package com.nikitin.roadmaps.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.nikitin.roadmaps.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.exception.JsonReadValueException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@UtilityClass
public class RestUtils {

    public <T> T convertResponseToDto(String response, Class<T> dto) {
        try {
            ObjectMapper jsonMapper = JsonMapper.builder()
                    .findAndAddModules()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .build();
            return jsonMapper.readValue(response, dto);
        } catch (Exception exception) {
            throw new JsonReadValueException(exception.getMessage());
        }
    }
}
