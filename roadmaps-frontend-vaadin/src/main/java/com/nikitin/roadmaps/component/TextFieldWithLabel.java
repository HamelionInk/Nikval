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

    public TextFieldWithLabel() {
        paragraph = new Paragraph();
        paragraph.addClassName("paragraph");

        textField = new TextField();
        textField.addClassName("text_field");

        add(paragraph, textField);
    }

    public void setReadOnly(Boolean readOnly) {
        textField.setReadOnly(readOnly);
    }

    public void setLabel(String text) {
        paragraph.setText(text);
    }

    public void setValue(String text) {
        textField.setValue(text);
    }

    public String getValue() {
        return this.textField.getValue();
    }
}
