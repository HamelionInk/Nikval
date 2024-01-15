package com.nikitin.roadmapfrontend.icon;

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
    DROP_DOWN_HORIZONTAL,
    FAVORITE,
    FAVORITE_WHITELIST,
    ARROW_LEFT,
    ARROW_RIGHT,
    CHEVRON_RIGHT;

    public SvgIcon create() {
        var iconResource = new StreamResource("sprite.svg",
                () -> getClass().getResourceAsStream("/static/sprite.svg"));

        return new SvgIcon(iconResource, name().toLowerCase(Locale.ENGLISH).replace("_", "-"));
    }
}
