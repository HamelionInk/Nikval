package com.nikitin.roadmaps.views.profile.div;

import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class UserInfoDiv extends Div implements LocaleChangeObserver {

    private TextField nameTextField;
    private TextField lastNameTextField;
    private TextField emailTextField;
    private TextField specialityTextField;
    private TextField competenceTextField;


    public UserInfoDiv() {
        setClassName("profile_info_div");

        add(buildAccordionPanel());
    }

    private AccordionPanel buildAccordionPanel() {
        nameTextField = new TextField("Имя");
        nameTextField.addClassName("profile_text_field");
        nameTextField.setReadOnly(true);

        lastNameTextField = new TextField("Фамилия");
        lastNameTextField.addClassName("profile_text_field");
        lastNameTextField.setReadOnly(true);

        emailTextField = new TextField("Электронная почта");
        emailTextField.addClassName("profile_text_field");
        emailTextField.setReadOnly(true);

        specialityTextField = new TextField("Специализация");
        specialityTextField.addClassName("profile_text_field");
        specialityTextField.setReadOnly(true);

        competenceTextField = new TextField("Компетенция");
        competenceTextField.addClassName("profile_text_field");
        competenceTextField.setReadOnly(true);

        AccordionPanel accordionPanel = new AccordionPanel("Информация о пользователе");
        accordionPanel.addClassName("profile_accordion_panel");
        accordionPanel.setOpened(true);
        accordionPanel.addContent(nameTextField, lastNameTextField, emailTextField, specialityTextField, competenceTextField);

        return accordionPanel;
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
