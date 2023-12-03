package com.nikitin.roadmapfrontend.view;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Admin")
@RolesAllowed(value = {"ROLE_ADMIN"})
@Route(value = "admin", layout = MainView.class)
public class AdminView extends VerticalLayout {

    private final Paragraph viewName = new Paragraph("Панель администратора");

    public AdminView() {
        viewName.addClassName("view-name");

        add(viewName);
    }
}
