package com.nikitin.roadmapfrontend.view;

import com.nikitin.roadmapfrontend.client.ProfileClient;
import com.nikitin.roadmapfrontend.component.ViewHeader;
import com.nikitin.roadmapfrontend.dialog.ProfileAvatarDialog;
import com.nikitin.roadmapfrontend.utils.enums.CompetenceType;
import com.nikitin.roadmapfrontend.utils.enums.VaadinSessionAttribute;
import com.nikitin.roadmapfrontend.dto.request.ProfileRequestDto;
import com.nikitin.roadmapfrontend.utils.SessionHelper;
import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RolesAllowed(value = {"ROLE_USER", "ROLE_ADMIN"})
@Route(value = "profile", layout = MainView.class)
public class ProfileView extends VerticalLayout implements View {

    private final ProfileClient profileClient;

    private final Button uploadButton = new Button("Загрузить");
    private final Button editButton = new Button("Редактировать");
    private final Button saveButton = new Button("Сохранить");
    private final Button cancelButton = new Button("Отменить");
    private final HorizontalLayout headerLayout = new HorizontalLayout();
    private final VerticalLayout profileLayout = new VerticalLayout();
    private final FormLayout profileFormLayout = new FormLayout();
    private final Avatar profileAvatar = new Avatar();

    private final TextField profileName = new TextField("Имя пользователя");
    private final TextField profileEmail = new TextField("Электронная почта");
    private final DatePicker profileBirthDate = new DatePicker("Дата рождения");
    private final TextField profileResidentialAddress = new TextField("Место проживания");
    private final ComboBox<CompetenceType> profileCompetence = new ComboBox<>("Компетенция");
    private final TextField profileSpeciality = new TextField("Специальность");
    private final TextArea learnedChapter = new TextArea("Изученные разделы");
    private final TextArea learnedTopic = new TextArea("Изученные темы");
    private final TextArea plannedLearningQuestion = new TextArea("Запланированные вопросы для изучения");


    public ProfileView(@Autowired ProfileClient profileClient) {
        this.profileClient = profileClient;

        addClassName("profile-view");
        add(buildViewHeader(), buildProfileLayout());
        refreshView();
    }

    private ViewHeader buildViewHeader() {
        var viewHeader = new ViewHeader("Профиль");

        editButton.setVisible(true);
        uploadButton.setVisible(false);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);

        uploadButton.addClickListener(event -> new ProfileAvatarDialog<>(this).open());

        saveButton.addClickListener(event -> {
            saveData();
            changeEditedMode();
        });

        cancelButton.addClickListener(event -> changeEditedMode());

        editButton.addClickListener(event -> changeEditedMode());

        viewHeader.addButton(uploadButton, saveButton, cancelButton, editButton);

        return viewHeader;
    }

    private VerticalLayout buildProfileLayout() {
        headerLayout.addClassName("profile-header-layout");
        profileFormLayout.addClassName("profile-from-layout");
        profileLayout.addClassName("profile-layout");
        profileAvatar.addClassName("profile-avatar");

        profileCompetence.setItems(CompetenceType.getAllValue());
        profileCompetence.setItemLabelGenerator(CompetenceType::getName);

        profileFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 2));

        Stream.<Component>of(profileName, profileEmail, profileBirthDate, profileResidentialAddress, profileSpeciality, profileCompetence,
                learnedChapter, learnedTopic, plannedLearningQuestion).forEach(field -> {
            field.addClassName("profile_field");
            ((AbstractSinglePropertyField<?, ?>) field).setReadOnly(true);
            profileFormLayout.add(field);
        });

        headerLayout.add(profileAvatar, profileFormLayout);
        profileLayout.add(headerLayout, learnedChapter, learnedTopic, plannedLearningQuestion);

        return profileLayout;
    }

    private void changeEditedMode() {
        Stream.<Component>of(profileName, profileEmail, profileBirthDate, profileResidentialAddress, profileSpeciality, profileCompetence)
                .forEach(field -> ((AbstractSinglePropertyField<?, ?>) field)
                        .setReadOnly(!((AbstractSinglePropertyField<?, ?>) field).isReadOnly()));

        Stream.<Component>of(saveButton, cancelButton, editButton, uploadButton).forEach(button ->
                button.setVisible(!button.isVisible()));

        updateData();
    }

    private void saveData() {
        profileClient.patch((Long) SessionHelper.getSessionAttribute(VaadinSessionAttribute.PROFILE_ID),
                ProfileRequestDto.builder()
                        .name(hasValue(profileName.getValue().split(" ")[0]))
                        .lastName(hasValue(profileName.getValue().split(" ")[1]))
                        .email(hasValue(profileEmail.getValue()))
                        .speciality(hasValue(profileSpeciality.getValue()))
                        .competence(profileCompetence.getValue())
                        .birthDate((Objects.nonNull(profileBirthDate.getValue()) ?
                                profileBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant() : null))
                        .residentialAddress(hasValue(profileResidentialAddress.getValue()))
                        .build());
    }

    private void updateData() {
        var profileResponseDto = profileClient.getById((Long) SessionHelper.getSessionAttribute(VaadinSessionAttribute.PROFILE_ID));

        Optional.ofNullable(profileResponseDto.getFullName())
                .ifPresent(profileName::setValue);

        Optional.ofNullable(profileResponseDto.getEmail())
                .ifPresent(profileEmail::setValue);

        Optional.ofNullable(profileResponseDto.getBirthDate())
                .ifPresent(birthDate -> profileBirthDate.setValue(LocalDate.ofInstant(birthDate, ZoneId.systemDefault())));

        Optional.ofNullable(profileResponseDto.getResidentialAddress())
                .ifPresent(profileResidentialAddress::setValue);

        Optional.ofNullable(profileResponseDto.getCompetence())
                .ifPresent(profileCompetence::setValue);

        Optional.ofNullable(profileResponseDto.getSpeciality())
                .ifPresent(profileSpeciality::setValue);

        Optional.ofNullable(profileResponseDto.getPicture())
                .ifPresent(profileAvatar::setImage);
    }

    @Override
    public void refreshView() {
        updateData();
    }

    @Override
    public <T> T getClient(Class<T> clientType) {
        if (clientType.isInstance(profileClient)) {
            return clientType.cast(profileClient);
        }

        //todo add exception
        return null;
    }
}
