package com.nikitin.roadmaps.views.homepage;

import com.nikitin.roadmaps.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PageTitle("Home page")
@Route(value = "homePage", layout = MainLayout.class)
@PermitAll
public class HomePageView extends VerticalLayout {

    public HomePageView() {
        add(new Text("HomePageView"));
    }
}
