package com.nikitin.roadmaps.views.profile.button;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
@Setter
public class UserInfoEditButton extends Button {

    private Boolean isActive;

    public UserInfoEditButton(String text, Component icon) {
        super(text, icon);
        isActive = false;
    }
}
