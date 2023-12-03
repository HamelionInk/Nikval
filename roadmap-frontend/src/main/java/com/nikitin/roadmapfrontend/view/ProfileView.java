package com.nikitin.roadmapfrontend.view;

import com.nikitin.roadmapfrontend.client.ProfileClient;
import com.nikitin.roadmapfrontend.component.ViewHeader;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@RolesAllowed(value = {"ROLE_USER", "ROLE_ADMIN"})
@Route(value = "profile", layout = MainView.class)
public class ProfileView extends VerticalLayout implements View {

    private final ProfileClient profileClient;

    private final Button editButton = new Button();
    private final HorizontalLayout headerLayout = new HorizontalLayout();
    private final FormLayout profileFormLayout = new FormLayout();
    private final Avatar profileAvatar = new Avatar();

    private final TextField profileName = new TextField();
    private final TextField profileEmail = new TextField();

    public ProfileView(@Autowired ProfileClient profileClient) {
        this.profileClient = profileClient;

        addClassName("profile-view");

        refreshView();
    }

    private ViewHeader buildViewHeader() {
        var viewHeader = new ViewHeader("Профиль");

        editButton.setText("Редактировать");
        viewHeader.addButton(editButton);

        return viewHeader;
    }

    private VerticalLayout buildProfileLayout() {
        var profileLayout = new VerticalLayout();
        profileLayout.addClassName("profile-layout");
        headerLayout.addClassName("profile-header-layout");
        profileFormLayout.addClassName("profile-from-layout");
        profileAvatar.addClassName("profile-avatar");
        profileName.addClassName("profile-name");
        profileEmail.addClassName("profile-email");

        profileName.setLabel("Имя пользователя");
        profileEmail.setLabel("Электронная почта");

        profileFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 2));
        profileFormLayout.add(profileName, profileEmail);

        headerLayout.add(profileAvatar, profileFormLayout);

        profileLayout.add(headerLayout);

        return profileLayout;
    }

    @Override
    public void refreshView() {
        removeAll();

        add(buildViewHeader(), buildProfileLayout());
    }

    @Override
    public <T> T getClient(Class<T> clientType) {
        return null;
    }
}
