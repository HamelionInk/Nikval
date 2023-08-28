package com.nikitin.roadmaps.views.profile.div;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class SaveInfoDiv extends Div {

    private Button saveUserInfoButton;

    public SaveInfoDiv() {
        addClassName("save_info_div");

        saveUserInfoButton = new Button("Сохранить", VaadinIcon.SAFE.create());
        saveUserInfoButton.addClassName("save_user_info_button");
        saveUserInfoButton.setVisible(false);

        add(saveUserInfoButton);
    }
}
