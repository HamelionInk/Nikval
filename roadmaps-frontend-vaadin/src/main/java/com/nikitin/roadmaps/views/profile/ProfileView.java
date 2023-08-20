package com.nikitin.roadmaps.views.profile;

import com.nikitin.roadmaps.views.MainLayout;
import com.nikitin.roadmaps.views.profile.div.RoadmapInfoDiv;
import com.nikitin.roadmaps.views.profile.div.UserInfoDiv;
import com.nikitin.roadmaps.views.profile.layout.HeaderProfileLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@Slf4j
@PageTitle("Profile")
@Route(value = "profile", layout = MainLayout.class)
@PermitAll
public class ProfileView extends VerticalLayout implements LocaleChangeObserver {

    private HeaderProfileLayout headerProfileLayout;
    private UserInfoDiv userInfoDiv;
    private RoadmapInfoDiv roadmapInfoDiv;

    public ProfileView(AuthenticationContext authenticationContext) {
        headerProfileLayout = new HeaderProfileLayout();
        userInfoDiv = new UserInfoDiv();
        roadmapInfoDiv = new RoadmapInfoDiv();

        add(headerProfileLayout, userInfoDiv, roadmapInfoDiv);

        addEventListeners();
    }

    public void addEventListeners() {
        headerProfileLayout.getUserInfoEditButton().addClickListener(click -> {
            userInfoDiv.getNameTextField().setReadOnly(false);
            userInfoDiv.getLastNameTextField().setReadOnly(false);
            userInfoDiv.getEmailTextField().setReadOnly(false);
            userInfoDiv.getSpecialityTextField().setReadOnly(false);
            userInfoDiv.getCompetenceTextField().setReadOnly(false);
            headerProfileLayout.getSaveUserInfoButton().setVisible(true);
        });

        headerProfileLayout.getSaveUserInfoButton().addClickListener(click -> {
            userInfoDiv.getNameTextField().setReadOnly(true);
            userInfoDiv.getLastNameTextField().setReadOnly(true);
            userInfoDiv.getEmailTextField().setReadOnly(true);
            userInfoDiv.getSpecialityTextField().setReadOnly(true);
            userInfoDiv.getCompetenceTextField().setReadOnly(true);
            headerProfileLayout.getSaveUserInfoButton().setVisible(false);
        });
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
