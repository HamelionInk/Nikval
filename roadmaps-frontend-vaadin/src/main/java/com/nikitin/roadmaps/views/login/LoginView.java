package com.nikitin.roadmaps.views.login;

import com.nikitin.roadmaps.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Login")
@Route(value = "login", layout = MainLayout.class)
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver, LocaleChangeObserver {

    private static final String GOOGLE_OAUTH_URL = "/oauth2/authorization/google";

    private static final String FORM_ADDITIONAL_INFORMATION_KEY = "login.view.additional_information";
    private static final String FORM_TITLE_KEY = "login.view.form_title";
    private static final String FORM_USERNAME_KEY = "login.view.username";
    private static final String FORM_PASSWORD_KEY = "login.view.password";
    private static final String FORM_SUBMIT_KEY = "login.view.form_submit";
    private static final String FORM_FORGOT_PASSWORD_KEY = "login.view.forgot_password";
    private static final String ERROR_TITLE_KEY = "login.view.error_title";
    private static final String ERROR_MESSAGE_KEY = "login.view.error_message";

    private final LoginForm loginForm;
    private final LoginI18n i18n = LoginI18n.createDefault();
    private final LoginI18n.Form i18nForm = i18n.getForm();
    private final LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();

    public LoginView() {
        Image googleLogo = new Image("images/google-logo.png", "Google");
        googleLogo.addClassName("googleLogo");

        Button googleLoginButton = new Button("", googleLogo, click -> getUI().ifPresent(UI -> UI.getPage().setLocation(GOOGLE_OAUTH_URL)));
        googleLoginButton.addClassName("googleLoginButton");

        Div oAuth2Logins = new Div();
        oAuth2Logins.addClassName("oAuth2Logins");
        oAuth2Logins.add(googleLoginButton);

        i18n.setForm(i18nForm);
        i18n.setErrorMessage(i18nErrorMessage);

        loginForm = new LoginForm();
        loginForm.addClassName("loginForm");
        loginForm.setAction("login");

        add(loginForm, oAuth2Logins);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        i18n.setAdditionalInformation(getTranslation(FORM_ADDITIONAL_INFORMATION_KEY));
        i18nForm.setTitle(getTranslation(FORM_TITLE_KEY));
        i18nForm.setUsername(getTranslation(FORM_USERNAME_KEY));
        i18nForm.setPassword(getTranslation(FORM_PASSWORD_KEY));
        i18nForm.setSubmit(getTranslation(FORM_SUBMIT_KEY));
        i18nForm.setForgotPassword(getTranslation(FORM_FORGOT_PASSWORD_KEY));
        i18nErrorMessage.setTitle(getTranslation(ERROR_TITLE_KEY));
        i18nErrorMessage.setMessage(getTranslation(ERROR_MESSAGE_KEY));
        loginForm.setI18n(i18n);
    }
}
