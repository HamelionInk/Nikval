package com.nikitin.roadmapfrontend.utils.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum RoadmapType {

    ALL("Все"),
    MY("Мои"),
    FAVORITE("Избранные"),
    DEFAULT("Готовые");

    private final String name;

    RoadmapType(String name) {
        this.name = name;
    }

    public static List<RoadmapType> getAllValue() {
        return Arrays.stream(RoadmapType.values()).toList();
    }
}
