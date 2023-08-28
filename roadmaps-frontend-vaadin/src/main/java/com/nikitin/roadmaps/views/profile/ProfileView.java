package com.nikitin.roadmaps.views.profile;

import com.nikitin.roadmaps.client.ProfileClient;
import com.nikitin.roadmaps.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.dto.response.ProfileResponseDto;
import com.nikitin.roadmaps.views.MainLayout;
import com.nikitin.roadmaps.views.profile.div.RoadmapInfoDiv;
import com.nikitin.roadmaps.views.profile.div.SaveInfoDiv;
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
import com.nikitin.roadmaps.util.ViewUtils;

@Slf4j
@PageTitle("Profile")
@Route(value = "profile", layout = MainLayout.class)
@PermitAll
public class ProfileView extends VerticalLayout implements LocaleChangeObserver {

    private ProfileClient profileClient;
    private HeaderProfileLayout headerProfileLayout;
    private UserInfoDiv userInfoDiv;
    private RoadmapInfoDiv roadmapInfoDiv;
    private SaveInfoDiv saveInfoDiv;

    private ProfileResponseDto profileResponseDto;

    public ProfileView(@Autowired ProfileClient profileClient) {
        this.profileClient = profileClient;

        headerProfileLayout = new HeaderProfileLayout();
        userInfoDiv = new UserInfoDiv();
        roadmapInfoDiv = new RoadmapInfoDiv();
        saveInfoDiv = new SaveInfoDiv();

        add(headerProfileLayout, userInfoDiv, roadmapInfoDiv, saveInfoDiv);
        getProfile();
        addEventListeners();
    }

    private void setProfile(ProfileResponseDto profileResponseDto) {
        Optional.ofNullable(profileResponseDto.getName())
                .ifPresent(name -> userInfoDiv.getNameTextField().setValue(name));

        Optional.ofNullable(profileResponseDto.getLastName())
                .ifPresent(lastName -> userInfoDiv.getLastNameTextField().setValue(lastName));

        Optional.ofNullable(profileResponseDto.getEmail())
                .ifPresent(email -> userInfoDiv.getEmailTextField().setValue(email));

        Optional.ofNullable(profileResponseDto.getCompetence())
                .ifPresent(competence -> userInfoDiv.getCompetenceTextField().setValue(competence.getName()));

        Optional.ofNullable(profileResponseDto.getSpeciality())
                .ifPresent(speciality -> userInfoDiv.getSpecialityTextField().setValue(speciality));

        Optional.ofNullable(profileResponseDto.getPicture())
                .ifPresent(picture -> headerProfileLayout.getUserAvatar().setImage(picture));

        Optional.ofNullable(profileResponseDto.getFullName())
                .ifPresent(fullName -> {
                    headerProfileLayout.getUserFullNameH3().setText(fullName);
                    headerProfileLayout.getUserAvatar().setName(fullName);
                });

        Optional.ofNullable(profileResponseDto.getLastDateLogin())
                .ifPresent(lastDateLogin -> headerProfileLayout.getUserLoginDateH4().setText(lastDateLogin.toString()));
    }

    public void addEventListeners() {
        headerProfileLayout.getUserInfoEditButton().addClickListener(click -> {
            if (headerProfileLayout.getUserInfoEditButton().getIsActive()) {
                readOnlyFieldsStatus(true);
                saveInfoDiv.getSaveUserInfoButton().setVisible(false);
                headerProfileLayout.getUserInfoEditButton().setIsActive(false);
                setProfile(profileResponseDto);
            } else {
                readOnlyFieldsStatus(false);
                saveInfoDiv.getSaveUserInfoButton().setVisible(true);
                headerProfileLayout.getUserInfoEditButton().setIsActive(true);
            }
        });

        saveInfoDiv.getSaveUserInfoButton().addClickListener(click -> {
            readOnlyFieldsStatus(true);
            saveInfoDiv.getSaveUserInfoButton().setVisible(false);
            headerProfileLayout.getUserInfoEditButton().setIsActive(false);
            patchProfile(ProfileRequestDto.builder()
                    .name(ViewUtils.hasStringValue(userInfoDiv.getNameTextField().getValue()))
                    .lastName(ViewUtils.hasStringValue(userInfoDiv.getLastNameTextField().getValue()))
                    .email(ViewUtils.hasStringValue(userInfoDiv.getEmailTextField().getValue()))
                    //todo - Сделать Enum
                    //.competence(userInfoDiv.getCompetenceTextField().getValue())
                    .speciality(ViewUtils.hasStringValue(userInfoDiv.getSpecialityTextField().getValue()))
                    .build());
        });
    }

    private void readOnlyFieldsStatus(Boolean status) {
        userInfoDiv.getNameTextField().setReadOnly(status);
        userInfoDiv.getLastNameTextField().setReadOnly(status);
        userInfoDiv.getEmailTextField().setReadOnly(status);
        userInfoDiv.getSpecialityTextField().setReadOnly(status);
        userInfoDiv.getCompetenceTextField().setReadOnly(status);
    }

    private void getProfile() {
        var oidcUser = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = profileClient.getByEmail(oidcUser.getEmail());

        if (response.getStatusCode().is2xxSuccessful()) {
            Optional.ofNullable(response.getBody())
                    .ifPresent(body -> {
                        profileResponseDto = body;
                        setProfile(profileResponseDto);
                    });
        } else {
            //todo - прокидывать exception
            log.info("Get Profile Error");
        }
    }

    private void patchProfile(ProfileRequestDto profileRequestDto) {
        var response = profileClient.patch(profileResponseDto.getId(), profileRequestDto);

        if (response.getStatusCode().is2xxSuccessful()) {
            Optional.ofNullable(response.getBody())
                    .ifPresent(body -> {
                        profileResponseDto = body;
                        setProfile(profileResponseDto);
                    });
        } else {
            //todo - прокидывать exception
            log.info("Save Profile Error");
        }
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
