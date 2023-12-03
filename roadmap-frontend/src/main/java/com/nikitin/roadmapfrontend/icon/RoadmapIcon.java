package com.nikitin.roadmapfrontend.icon;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.server.StreamResource;

import java.util.Locale;

public enum RoadmapIcon {

    HOME,
    PROFILE,
    ROADMAP,
    SETTING,
    TOGGLE_MENU,
    EXIT,
    ADMIN,
    DROP_DOWN_VERTICAL,
    DROP_DOWN_HORIZONTAL;

    public SvgIcon create() {
        var iconResource = new StreamResource("sprite.svg",
                () -> getClass().getResourceAsStream("/static/sprite.svg"));

        var svgIcon = new SvgIcon(iconResource, name().toLowerCase(Locale.ENGLISH).replace("_", "-"));

        return svgIcon;
    }
}
