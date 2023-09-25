package com.nikitin.roadmaps.views.guide;

import com.nikitin.roadmaps.views.MainLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Route(value = "guide", layout = MainLayout.class)
@RolesAllowed(value = {"ROLE_USER"})
public class GuideView extends VerticalLayout {

    public GuideView() {
        add(new Paragraph("Guide"));
    }
}
