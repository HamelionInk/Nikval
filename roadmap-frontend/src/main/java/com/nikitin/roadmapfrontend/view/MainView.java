package com.nikitin.roadmapfrontend.view;

import com.nikitin.roadmapfrontend.client.ProfileClient;
import com.nikitin.roadmapfrontend.component.SideNavigationBar;
import com.nikitin.roadmapfrontend.component.SideNavigationMenuItem;
import com.nikitin.roadmapfrontend.configuration.security.SecurityService;
import com.nikitin.roadmapfrontend.utils.enums.VaadinSessionAttribute;
import com.nikitin.roadmapfrontend.icon.RoadmapIcon;
import com.nikitin.roadmapfrontend.utils.SessionHelper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MainView extends AppLayout implements BeforeLeaveObserver {

    public MainView(@Autowired ProfileClient profileClient) {
        addClassName("main-view");

        var sideNavigationMenuItems = new ArrayList<>(List.of(
                new SideNavigationMenuItem(RoadmapIcon.HOME, "Главная страница", HomeView.class),
                new SideNavigationMenuItem(RoadmapIcon.PROFILE, "Профиль", ProfileView.class),
                new SideNavigationMenuItem(RoadmapIcon.ROADMAP, "Карта развития", RoadmapsView.class),
                new SideNavigationMenuItem(RoadmapIcon.SETTING, "Настройки", SettingView.class)
        ));

        if (SecurityService.getAuthorities().contains("ROLE_ADMIN")) {
            sideNavigationMenuItems.add(new SideNavigationMenuItem(RoadmapIcon.ADMIN, "Панель администратора", AdminView.class));
        }

        var profileResponseDto = profileClient.getById((Long) SessionHelper.getSessionAttribute(VaadinSessionAttribute.PROFILE_ID));

        var sideNavigationBar = new SideNavigationBar(profileResponseDto, RoadmapIcon.TOGGLE_MENU, sideNavigationMenuItems);

        addToDrawer(sideNavigationBar);
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
        UI.getCurrent().getPage().executeJs("return window.location.href").then(String.class, location ->
                UI.getCurrent().getSession().setAttribute(VaadinSessionAttribute.PREVIOUS_URL.getValue(), location));
    }
}
