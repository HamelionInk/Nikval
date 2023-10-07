package com.nikitin.roadmaps.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class CustomDialog extends Dialog {

    private final Div headerDiv = new Div();
    private final VerticalLayout contentLayout = new VerticalLayout();
    private final Paragraph headerName = new Paragraph();
    private final Button closeButton = new Button();

    public CustomDialog() {
        addClassName("dialog");
        setWidth("450px");
        setDraggable(true);

        configurationHeader();
        configurationContent();
    }

    public void setHeaderNameText(String text) {
        headerName.setText(text);
    }

    private void configurationHeader() {
        headerName.addClassName("dialog_header_name");

        closeButton.setIcon(VaadinIcon.CLOSE_SMALL.create());
        closeButton.addClassName("dialog_close_button");
        closeButton.addClickListener(event -> close());

        headerDiv.addClassName("dialog_header_div");
        headerDiv.add(headerName, closeButton);

        getHeader().add(headerDiv);
    }

    private void configurationContent() {
        contentLayout.addClassName("dialog_vertical_layout");
        add(contentLayout);
    }
}
