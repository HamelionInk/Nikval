package com.nikitin.roadmapfrontend.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class ViewHeader extends HorizontalLayout {

    private final Paragraph viewName = new Paragraph();

    public ViewHeader(Button... buttons) {
        addClassName("view-header");
        addButton(buttons);
    }

    public ViewHeader(String viewNameText, Button... buttons) {
        addClassName("view-header");
        addViewName(viewNameText);
        addButton(buttons);
    }

    public void addViewName(String viewNameText) {
        viewName.addClassName("view-name");
        viewName.setText(viewNameText);
        add(viewName);
    }

    public void addButton(Component... buttons) {
        Arrays.stream(buttons).forEach(button -> {
            button.addClassName("view-button");
            add(button);
        });
    }
}
