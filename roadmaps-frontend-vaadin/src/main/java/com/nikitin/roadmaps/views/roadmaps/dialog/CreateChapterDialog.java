package com.nikitin.roadmaps.views.roadmaps.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateChapterDialog extends Dialog {

    private H3 headerNameH3;
    private TextField nameChapterTextField;
    private Button closeButton;
    private Button createButton;

    public CreateChapterDialog() {
        addClassName("create_chapter_dialog");

        buildComponents();

        getHeader().add(headerNameH3, closeButton);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addClassName("dialog_vertical_layout");
        verticalLayout.add(nameChapterTextField, createButton);
        add(verticalLayout);
    }

    private void buildComponents() {
        headerNameH3 = new H3("Создать раздел");
        headerNameH3.addClassName("header_name_h3");

        closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        closeButton.addClassName("close_button");
        closeButton.addClickListener(event -> close());

        nameChapterTextField = new TextField("Название");
        nameChapterTextField.addClassName("name_chapter_text_field");

        createButton = new Button("Создать");
        createButton.addClassName("create_button");
    }
}
