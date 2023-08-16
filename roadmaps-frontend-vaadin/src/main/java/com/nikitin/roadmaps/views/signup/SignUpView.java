package com.nikitin.roadmaps.views.signup;

import com.nikitin.roadmaps.agent.PersonAgent;
import com.nikitin.roadmaps.dto.request.PersonRequestDto;
import com.nikitin.roadmaps.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.regex.Pattern;

@PageTitle("Sign-up")
@Route(value = "signUp", layout = MainLayout.class)
@AnonymousAllowed
public class SignUpView extends VerticalLayout implements LocaleChangeObserver {

    private final static String USERNAME_TEXT_FIELD_KEY = "signUp.view.usernameTextField";
    private final static String PASSWORD_FIELD_KEY = "signUp.view.passwordField";
    private final static String CONFIRM_PASSWORD_FIELD_KEY = "signUp.view.confirmPasswordField";
    private final static String EMAIL_FIELD_KEY = "signUp.view.emailField";
    private final static String PAGE_NAME_KEY = "signUp.view.pageName";
    private final static String SUBMIT_BUTTON_KEY = "signUp.view.submitButton";
    private final static String ERROR_VALIDATION_KEY = "signUp.view.errorValidationKey";

    private Binder<PersonRequestDto> personRequestDtoBinder;
    private NativeLabel signUpSuccess;
    private TextField usernameTextField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button submitButton;
    private H1 pageName;

    public SignUpView() {
        buildComponents();
        binderSignUpForm();
    }

    private void buildComponents() {
        //todo - перевод сообщений
        signUpSuccess = new NativeLabel("Аккаунт успешно зарегистрирован");
        signUpSuccess.addClassName("signUpSuccess");
        signUpSuccess.setVisible(false);

        usernameTextField = new TextField(getTranslation(USERNAME_TEXT_FIELD_KEY));
        usernameTextField.setRequired(true);

        passwordField = new PasswordField(getTranslation(PASSWORD_FIELD_KEY));
        passwordField.setRequired(true);

        confirmPasswordField = new PasswordField(getTranslation(CONFIRM_PASSWORD_FIELD_KEY));
        confirmPasswordField.setRequired(true);

        emailField = new TextField(getTranslation(EMAIL_FIELD_KEY));
        emailField.setRequired(true);

        pageName = new H1(getTranslation(PAGE_NAME_KEY));
        pageName.setClassName("pageName");

        personRequestDtoBinder = new BeanValidationBinder<>(PersonRequestDto.class);

        submitButton = new Button(getTranslation(SUBMIT_BUTTON_KEY), click -> {
            var personRequestDto = new PersonRequestDto();
            if (personRequestDtoBinder.writeBeanIfValid(personRequestDto)) {
                var response = PersonAgent.instance().createPerson(personRequestDto);
                whenSignUpSuccess(response.getStatusCode());
            } else {
                personRequestDtoBinder.validate();
            }
        });
        submitButton.addClassName("submitButton");

        FormLayout signUpForm = new FormLayout();
        signUpForm.addClassName("signUpForm");
        signUpForm.add(
                signUpSuccess,
                usernameTextField,
                passwordField,
                confirmPasswordField,
                emailField);

        add(pageName, signUpForm, submitButton);
    }

    private void binderSignUpForm() {
        //todo - перевод сообщений
        personRequestDtoBinder.forField(usernameTextField)
                .withValidator(
                        StringUtils::hasText,
                        "Поле не заполнено")
                .withValidator(username ->
                                username.length() >= 4,
                        "Поле должно быть > 3 символов")
                .bind(PersonRequestDto::getUsername, PersonRequestDto::setUsername);

        personRequestDtoBinder.forField(passwordField)
                .withValidator(
                        StringUtils::hasText,
                        "Поле не заполнено"
                )
                .withValidator(password ->
                                password.length() >= 6,
                        "Поле должно быть > 5 символов")
                .bind(PersonRequestDto::getPassword, PersonRequestDto::setPassword);

        personRequestDtoBinder.forField(confirmPasswordField)
                .withValidator(
                        StringUtils::hasText,
                        "Поле не заполнено"
                )
                .withValidator(confirmPassword ->
                                confirmPassword.equals(passwordField.getValue()),
                        "Пароли не совпадают"
                )
                .bind(PersonRequestDto::getConfirmPassword, PersonRequestDto::setConfirmPassword);

        personRequestDtoBinder.forField(emailField)
                .withValidator(
                        StringUtils::hasText,
                        "Поле не заполнено"
                )
                .withValidator(email ->
                                Pattern.compile("^([a-zA-Z0-9_\\.\\-+])+@[a-zA-Z0-9-.]+\\.[a-zA-Z0-9-]{2,}$")
                                .matcher(email)
                                .matches(),
                        "Неверный формат почты")
                .bind(PersonRequestDto::getEmail, PersonRequestDto::setEmail);

        confirmPasswordField.addValueChangeListener(
                event -> personRequestDtoBinder.validate());
    }

    private void whenSignUpSuccess(HttpStatusCode httpStatusCode) {
        if(httpStatusCode.is2xxSuccessful()) {
            signUpSuccess.setVisible(true);
        }

        if(Objects.equals(httpStatusCode.value(), HttpStatus.CONFLICT.value())) {
            signUpSuccess.setText("Аккаунт уже существует");
            signUpSuccess.setVisible(true);
        }
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        usernameTextField.setLabel(getTranslation(USERNAME_TEXT_FIELD_KEY));
        passwordField.setLabel(getTranslation(PASSWORD_FIELD_KEY));
        confirmPasswordField.setLabel(getTranslation(CONFIRM_PASSWORD_FIELD_KEY));
        emailField.setLabel(getTranslation(EMAIL_FIELD_KEY));
        pageName.setText(getTranslation(PAGE_NAME_KEY));
        submitButton.setText(getTranslation(SUBMIT_BUTTON_KEY));
        usernameTextField.setErrorMessage(getTranslation(ERROR_VALIDATION_KEY));
        passwordField.setErrorMessage(getTranslation(ERROR_VALIDATION_KEY));
        confirmPasswordField.setErrorMessage(getTranslation(ERROR_VALIDATION_KEY));
        emailField.setErrorMessage(getTranslation(ERROR_VALIDATION_KEY));
    }
}
