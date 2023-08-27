package com.nikitin.roadmaps.roadmapsbackendspring.dto.enums;

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
}
