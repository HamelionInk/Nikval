package com.nikitin.roadmaps.views.profile;

import com.nikitin.roadmaps.client.ProfileClient;
import com.nikitin.roadmaps.views.MainLayout;
import com.nikitin.roadmaps.views.profile.div.RoadmapInfoDiv;
import com.nikitin.roadmaps.views.profile.div.UserInfoDiv;
import com.nikitin.roadmaps.views.profile.layout.HeaderProfileLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Optional;

@Slf4j
@PageTitle("Profile")
@Route(value = "profile", layout = MainLayout.class)
@PermitAll
public class ProfileView extends VerticalLayout implements LocaleChangeObserver {

    private ProfileClient profileClient;
    private HeaderProfileLayout headerProfileLayout;
    private UserInfoDiv userInfoDiv;
    private RoadmapInfoDiv roadmapInfoDiv;

    public ProfileView(@Autowired ProfileClient profileClient) {
        this.profileClient = profileClient;

        headerProfileLayout = new HeaderProfileLayout();
        userInfoDiv = new UserInfoDiv();
        roadmapInfoDiv = new RoadmapInfoDiv();

        add(headerProfileLayout, userInfoDiv, roadmapInfoDiv);
        setProfileInfo();

        addEventListeners();
    }

    public void setProfileInfo() {
        var oidcUser = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var profile = profileClient.getByEmail(oidcUser.getEmail());

        Optional.ofNullable(profile.getName())
                .ifPresent(name -> userInfoDiv.getNameTextField().setValue(name));

        Optional.ofNullable(profile.getLastName())
                .ifPresent(lastName -> userInfoDiv.getLastNameTextField().setValue(lastName));

        Optional.ofNullable(profile.getEmail())
                .ifPresent(email -> userInfoDiv.getEmailTextField().setValue(email));

        Optional.ofNullable(profile.getCompetence())
                .ifPresent(competence -> userInfoDiv.getCompetenceTextField().setValue(competence.getName()));

        Optional.ofNullable(profile.getSpeciality())
                .ifPresent(speciality -> userInfoDiv.getSpecialityTextField().setValue(speciality));

        Optional.ofNullable(profile.getPicture())
                .ifPresent(picture -> headerProfileLayout.getUserAvatar().setImage(picture));

        Optional.ofNullable(profile.getFullName())
                .ifPresent(fullName -> {
                    headerProfileLayout.getUserFullNameH3().setText(fullName);
                    headerProfileLayout.getUserAvatar().setName(fullName);
                });

        Optional.ofNullable(profile.getLastDateLogin())
                .ifPresent(lastDateLogin -> headerProfileLayout.getUserLoginDateH4().setText(lastDateLogin.toString()));
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
