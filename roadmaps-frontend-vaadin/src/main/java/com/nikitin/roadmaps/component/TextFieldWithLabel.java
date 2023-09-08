package com.nikitin.roadmaps.component;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextFieldWithLabel extends HorizontalLayout {

    private Paragraph paragraph;
    private TextField textField;

    public TextFieldWithLabel(String labelName) {
        paragraph = new Paragraph(labelName);
        paragraph.addClassName("paragraph");

        textField = new TextField();
        textField.addClassName("text_field");

        add(paragraph, textField);
    }

    public String getValue() {
        return this.textField.getValue();
    }
}
