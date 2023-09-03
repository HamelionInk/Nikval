package com.nikitin.roadmaps.views.profile;

import com.nikitin.roadmaps.client.ProfileClient;
import com.nikitin.roadmaps.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.dto.response.ProfileResponseDto;
import com.nikitin.roadmaps.util.RestUtils;
import com.nikitin.roadmaps.util.ViewUtils;
import com.nikitin.roadmaps.views.MainLayout;
import com.nikitin.roadmaps.views.profile.dialog.AvatarEditDialog;
import com.nikitin.roadmaps.views.profile.div.RoadmapInfoDiv;
import com.nikitin.roadmaps.views.profile.div.SaveInfoDiv;
import com.nikitin.roadmaps.views.profile.div.UserInfoDiv;
import com.nikitin.roadmaps.views.profile.layout.HeaderProfileLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@PageTitle("Profile")
@Route(value = "profile", layout = MainLayout.class)
@RolesAllowed(value = {"ROLE_USER"})
public class ProfileView extends VerticalLayout implements LocaleChangeObserver {

    private ProfileClient profileClient;
    private HeaderProfileLayout headerProfileLayout;
    private UserInfoDiv userInfoDiv;
    private RoadmapInfoDiv roadmapInfoDiv;
    private SaveInfoDiv saveInfoDiv;

    private ProfileResponseDto profileResponseDto;
    private final Binder<ProfileRequestDto> profileRequestDtoBinder;

    public ProfileView(@Autowired ProfileClient profileClient) {
        this.profileClient = profileClient;
        this.profileRequestDtoBinder = new BeanValidationBinder<>(ProfileRequestDto.class);

        headerProfileLayout = new HeaderProfileLayout();
        userInfoDiv = new UserInfoDiv();
        roadmapInfoDiv = new RoadmapInfoDiv();
        saveInfoDiv = new SaveInfoDiv();

        profileResponseDto = new ProfileResponseDto();

        binderProfileForm();
        addEventListeners();
        getProfile();

        add(headerProfileLayout, userInfoDiv, roadmapInfoDiv, saveInfoDiv);
    }

    private void setProfile(ProfileResponseDto profileResponseDto) {
        Optional.ofNullable(profileResponseDto.getName())
                .ifPresent(name -> userInfoDiv.getNameTextField().setValue(name));

        Optional.ofNullable(profileResponseDto.getLastName())
                .ifPresent(lastName -> userInfoDiv.getLastNameTextField().setValue(lastName));

        Optional.ofNullable(profileResponseDto.getEmail())
                .ifPresent(email -> userInfoDiv.getEmailTextField().setValue(email));

        Optional.ofNullable(profileResponseDto.getCompetence())
                .ifPresent(competence -> userInfoDiv.getCompetenceComboBox().setValue(competence));

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

    private void getProfile() {
        var oidcUser = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = profileClient.getByEmail(oidcUser.getEmail(), true);

        if (response.getStatusCode().is2xxSuccessful()) {
            Optional.ofNullable(response.getBody())
                    .ifPresent(body -> profileResponseDto = RestUtils.convertResponseToDto(body, ProfileResponseDto.class));
        }

        setProfile(profileResponseDto);
    }

    private void patchProfile(ProfileRequestDto profileRequestDto) {
        var response = profileClient.patch(profileResponseDto.getId(), profileRequestDto, true);

        if (response.getStatusCode().is2xxSuccessful()) {
            Optional.ofNullable(response.getBody())
                    .ifPresent(body -> profileResponseDto = RestUtils.convertResponseToDto(body, ProfileResponseDto.class));
        }

        setProfile(profileResponseDto);
    }

    private void binderProfileForm() {
        profileRequestDtoBinder.forField(userInfoDiv.getNameTextField())
                .withValidator(
                        StringUtils::hasText,
                        "Поле не заполнено")
                .withValidator(name -> name.length() >= 3,
                        "Поле должно быть > 2 символов")
                .bind(ProfileRequestDto::getName, ProfileRequestDto::setName);

        profileRequestDtoBinder.forField(userInfoDiv.getLastNameTextField())
                .withValidator(
                        StringUtils::hasText,
                        "Поле не заполнено")
                .withValidator(lastName -> lastName.length() >= 3,
                        "Поле должно быть > 2 символов")
                .bind(ProfileRequestDto::getLastName, ProfileRequestDto::setLastName);

        profileRequestDtoBinder.forField(userInfoDiv.getEmailTextField())
                .withValidator(
                        StringUtils::hasText,
                        "Поле не заполнено")
                .withValidator(email -> Pattern.compile("^([a-zA-Z0-9_\\-+])+@[a-zA-Z0-9-.]+\\.[a-zA-Z0-9-]{2,}$")
                                .matcher(email)
                                .matches(),
                        "Неверный формат почты")
                .bind(ProfileRequestDto::getEmail, ProfileRequestDto::setEmail);
    }

    private void addEventListeners() {
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
            if (profileRequestDtoBinder.isValid()) {
                readOnlyFieldsStatus(true);
                saveInfoDiv.getSaveUserInfoButton().setVisible(false);
                headerProfileLayout.getUserInfoEditButton().setIsActive(false);
                patchProfile(ProfileRequestDto.builder()
                        .name(ViewUtils.hasStringValue(userInfoDiv.getNameTextField().getValue()))
                        .lastName(ViewUtils.hasStringValue(userInfoDiv.getLastNameTextField().getValue()))
                        .email(ViewUtils.hasStringValue(userInfoDiv.getEmailTextField().getValue()))
                        .competence(userInfoDiv.getCompetenceComboBox().getValue())
                        .speciality(ViewUtils.hasStringValue(userInfoDiv.getSpecialityTextField().getValue()))
                        .build());
            } else {
                profileRequestDtoBinder.validate();
            }
        });

        headerProfileLayout.getAvatarEditDialog().getAvatarUpload().addSucceededListener(event -> {
           var response = profileClient.uploadAvatar(profileResponseDto.getId(), headerProfileLayout.getAvatarEditDialog().getFileBuffer().getFileData(), true);
           if (response.getStatusCode().is2xxSuccessful()) {
               getProfile();
           }
        });
    }

    private void readOnlyFieldsStatus(Boolean status) {
        userInfoDiv.getNameTextField().setReadOnly(status);
        userInfoDiv.getLastNameTextField().setReadOnly(status);
        userInfoDiv.getEmailTextField().setReadOnly(status);
        userInfoDiv.getSpecialityTextField().setReadOnly(status);
        userInfoDiv.getCompetenceComboBox().setReadOnly(status);
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
