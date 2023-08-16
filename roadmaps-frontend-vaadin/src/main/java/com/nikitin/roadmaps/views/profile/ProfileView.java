package com.nikitin.roadmaps.views.profile;

import com.nikitin.roadmaps.security.SecurityService;
import com.nikitin.roadmaps.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Profile")
@Route(value = "profile", layout = MainLayout.class)
@PermitAll
public class ProfileView extends VerticalLayout {

    public ProfileView(@Autowired SecurityService securityService) {
        add(new Button("Выйти", buttonClickEvent -> securityService.logout()));
    }
}
