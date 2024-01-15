package com.nikitin.roadmapfrontend.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed(value = {"ROLE_USER", "ROLE_ADMIN"})
@Route(value = "setting", layout = MainView.class)
public class SettingView extends VerticalLayout {
}
