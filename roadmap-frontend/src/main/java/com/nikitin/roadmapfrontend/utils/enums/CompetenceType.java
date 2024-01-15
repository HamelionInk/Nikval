package com.nikitin.roadmapfrontend.utils.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum CompetenceType {

    JUNIOR("Junior"),
    JUNIOR_PLUS("Junior+"),
    MIDDLE("Middle"),
    MIDDLE_PLUS("Middle+"),
    SENIOR("Senior");

    public final String name;

    CompetenceType(String name) {
        this.name = name;
    }

    public static List<CompetenceType> getAllValue() {
        return Arrays.stream(CompetenceType.values()).toList();
    }
}
