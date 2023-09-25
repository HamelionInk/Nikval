package com.nikitin.roadmaps.views.profile;

import com.nikitin.roadmaps.client.ProfileClient;
import com.nikitin.roadmaps.dto.enums.CompetenceType;
import com.nikitin.roadmaps.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.dto.response.ProfileResponseDto;
import com.nikitin.roadmaps.util.RestUtils;
import com.nikitin.roadmaps.util.ViewUtils;
import com.nikitin.roadmaps.views.MainLayout;
import com.nikitin.roadmaps.views.profile.dialog.AvatarEditDialog;
import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.theme.lumo.LumoIcon;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@PageTitle("Profile")
@Route(value = "profile", layout = MainLayout.class)
@RolesAllowed(value = {"ROLE_USER"})
public class ProfileView extends VerticalLayout implements LocaleChangeObserver {

    private final HorizontalLayout headerLayout = new HorizontalLayout();
    private final FormLayout formLayout = new FormLayout();
    private final HorizontalLayout bottomLayout = new HorizontalLayout();

    private final Avatar avatar = new Avatar();
    private final AvatarEditDialog avatarEditDialog = new AvatarEditDialog();
    private final Paragraph fullName = new Paragraph("Тестович Тестов");
    private final Button editButton = new Button();
    private final Button logoutButton = new Button();
    private final Button editAvatarButton = new Button();

    private final TextField name = new TextField();
    private final TextField surname = new TextField();
    private final TextField email = new TextField();
    private final ComboBox<CompetenceType> competence = new ComboBox<>();
    private final TextField speciality = new TextField();

    private final Button cancelButton = new Button();
    private final Button saveButton = new Button();

    private ProfileClient profileClient;

    public ProfileView(@Autowired ProfileClient profileClient) {
        addClassName("profile_view");

        this.profileClient = profileClient;

        buildHeaderLayout();
        buildFormLayout();
        buildBottomLayout();

        add(headerLayout, formLayout, bottomLayout);
    }

    private void buildHeaderLayout() {
        headerLayout.addClassName("profile_header_layout");

        avatar.addClassName("profile_avatar");

        fullName.addClassName("profile_full_name");

        editButton.addClassName("profile_edit_button");
        editButton.setIcon(LumoIcon.EDIT.create());
        editButton.addClickListener(event -> changeEditState());

        logoutButton.addClassName("profile_logout_button");
        logoutButton.setIcon(VaadinIcon.SIGN_OUT.create());
        logoutButton.addClickListener(event -> logout());

        editAvatarButton.addClassName("profile_edit_avatar_button");
        editAvatarButton.setIcon(VaadinIcon.UPLOAD_ALT.create());
        editAvatarButton.addClickListener(event -> avatarEditDialog.open());
        editAvatarButton.setVisible(false);

        var buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("profile_button_layout");
        buttonLayout.add(editAvatarButton, editButton, logoutButton);

        avatarEditDialog.getAvatarUpload().addSucceededListener(event -> avatar.setImage(uploadAvatar()));

        headerLayout.add(avatar, fullName, buttonLayout);
    }

    private void buildFormLayout() {
        formLayout.addClassName("profile_form_layout");

        name.addClassName("profile_name");
        name.setLabel("Name");

        surname.addClassName("profile_surname");
        surname.setLabel("Surname");

        email.addClassName("profile_email");
        email.setLabel("Email");

        competence.addClassName("profile_competence");
        competence.setLabel("Competence");
        competence.setItems(CompetenceType.getAllValue());
        competence.setItemLabelGenerator(CompetenceType::getName);

        speciality.addClassName("profile_speciality");
        speciality.setLabel("Speciality");

        Stream.<Component>of(name, surname, email, competence, speciality).forEach(item ->
                ((AbstractSinglePropertyField<?, ?>) item).setReadOnly(true));

        formLayout.add(name, surname, email, competence, speciality);

        setFormLayout(getProfileById());
    }

    private void buildBottomLayout() {
        bottomLayout.addClassName("profile_bottom_layout");

        cancelButton.addClassName("profile_cancel_button");
        cancelButton.setText("Cancel");
        cancelButton.addClickListener(event -> changeEditState());

        saveButton.addClassName("profile_save_button");
        saveButton.setText("Save");
        saveButton.addClickListener(event -> {
            setFormLayout(saveProfileById());
            changeEditState();
        });

        var buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("profile_button_layout_2");
        buttonLayout.add(cancelButton, saveButton);

        Stream.<Component>of(cancelButton, saveButton).forEach(item -> item.setVisible(false));

        bottomLayout.add(buttonLayout);
    }

    private void changeEditState() {
        Stream.<Component>of(name, surname, email, competence, speciality).forEach(item ->
                ((AbstractSinglePropertyField<?, ?>) item).setReadOnly(!Boolean.TRUE.equals(((AbstractSinglePropertyField<?, ?>) item).isReadOnly())));
        Stream.<Component>of(cancelButton, saveButton, editAvatarButton).forEach(item ->
                item.setVisible(!Boolean.TRUE.equals(item.isVisible())));
    }

    private void logout() {
        try {
            VaadinServletRequest.getCurrent().logout();
        } catch (ServletException e) {
            Notification.show("Logout failed");
        }
    }

    private ProfileResponseDto getProfileById() {
        var response = profileClient.getById((long) UI.getCurrent().getSession().getAttribute("profileId"), true);

        if (response.getStatusCode().is2xxSuccessful()) {
            return RestUtils.convertResponseToDto(response.getBody(), ProfileResponseDto.class);
        } else {
            return ProfileResponseDto.builder().build();
        }
    }

    private ProfileResponseDto saveProfileById() {
        var profileRequestDto = ProfileRequestDto.builder()
                .name(ViewUtils.hasStringValue(name.getValue()))
                .lastName(ViewUtils.hasStringValue(surname.getValue()))
                .email(ViewUtils.hasStringValue(email.getValue()))
                .competence(competence.getValue())
                .speciality(ViewUtils.hasStringValue(speciality.getValue()))
                .build();

        var response = profileClient.patch(
                (long) UI.getCurrent().getSession().getAttribute("profileId"),
                profileRequestDto,
                true);

        if (response.getStatusCode().is2xxSuccessful()) {
            return RestUtils.convertResponseToDto(response.getBody(), ProfileResponseDto.class);
        } else {
            return ProfileResponseDto.builder().build();
        }
    }

    private String uploadAvatar() {
        var response = profileClient.uploadAvatar(
                (long) UI.getCurrent().getSession().getAttribute("profileId"),
                avatarEditDialog.getFileBuffer().getFileData(), true);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return "";
        }
    }

    private void setFormLayout(ProfileResponseDto profileResponseDto) {
        Optional.ofNullable(profileResponseDto.getFullName())
                        .ifPresent(fullName::setText);

        Optional.ofNullable(profileResponseDto.getPicture())
                        .ifPresent(avatar::setImage);

        Optional.ofNullable(profileResponseDto.getName())
                        .ifPresent(name::setValue);

        Optional.ofNullable(profileResponseDto.getLastName())
                        .ifPresent(surname::setValue);

        Optional.ofNullable(profileResponseDto.getEmail())
                        .ifPresent(email::setValue);

        Optional.ofNullable(profileResponseDto.getCompetence())
                        .ifPresent(competence::setValue);

        Optional.ofNullable(profileResponseDto.getSpeciality())
                        .ifPresent(speciality::setValue);
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
