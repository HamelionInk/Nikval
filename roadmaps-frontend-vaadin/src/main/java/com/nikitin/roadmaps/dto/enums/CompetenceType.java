package com.nikitin.roadmaps.dto.enums;

import java.util.Arrays;
import java.util.List;

public enum CompetenceType {

    JUNIOR("Junior"),
    JUNIOR_PLUS("Junior+"),
    MIDDLE("Middle"),
    MIDDLE_PLUS("Middle+"),
    SENIOR("Senior");

    public String name;

    CompetenceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<CompetenceType> getAllValue() {
        return Arrays.stream(CompetenceType.values()).toList();
    }
}
