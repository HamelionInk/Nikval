package com.nikitin.roadmaps.views.profile;

import com.nikitin.roadmaps.views.MainLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PageTitle("Profile")
@Route(value = "profile/progress", layout = MainLayout.class)
@RolesAllowed(value = {"ROLE_USER"})
public class ProfileProgressView extends VerticalLayout implements LocaleChangeObserver {

    public ProfileProgressView() {
        add(new Paragraph("Roadmaps Info"));
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
