package com.nikitin.roadmaps.views.home;

import com.nikitin.roadmaps.client.ProfileClient;
import com.nikitin.roadmaps.dto.response.ProfileResponseDto;
import com.nikitin.roadmaps.util.RestUtils;
import com.nikitin.roadmaps.views.profile.ProfileView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Home page")
@Route(value = "")
@RolesAllowed(value = {"ROLE_USER"})
public class HomeView extends VerticalLayout {

    private final Paragraph title = new Paragraph();
    private final Paragraph userName = new Paragraph();
    private final Button startButton = new Button();
    private final Button tutorialButton = new Button();
    private final HorizontalLayout buttonLayout = new HorizontalLayout();


    private final ProfileClient profileClient;

    public HomeView(@Autowired ProfileClient profileClient) {
        this.profileClient = profileClient;

        buildHomeView();

        addClassName("home_view");
        add(title, userName, buttonLayout);
    }

    private void buildHomeView() {
        title.addClassName("home_title");
        title.setText("Добро пожаловать!");

        userName.addClassName("home_user_name");
        userName.setText(getProfileById().getName());

        startButton.addClassName("home_button");
        startButton.setText("Начать");
        startButton.addClickListener(event -> UI.getCurrent().navigateToClient("http://localhost:8082/profile"));

        tutorialButton.addClassName("home_button");
        tutorialButton.setText("Руководство");

        buttonLayout.addClassName("home_button_layout");
        buttonLayout.add(startButton, tutorialButton);
    }

    private ProfileResponseDto getProfileById() {
        var response = profileClient.getById((long) UI.getCurrent().getSession().getAttribute("profileId"), true);

        if (response.getStatusCode().is2xxSuccessful()) {
            return RestUtils.convertResponseToDto(response.getBody(), ProfileResponseDto.class);
        }

        return ProfileResponseDto.builder().name("Незнакомец").build();
    }
}
