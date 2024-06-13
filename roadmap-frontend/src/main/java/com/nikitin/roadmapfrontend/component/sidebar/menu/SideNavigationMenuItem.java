package com.nikitin.roadmapfrontend.component.sidebar.menu;

import com.nikitin.roadmapfrontend.icon.RoadmapIcon;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.router.RouterLink;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SideNavigationMenuItem extends RouterLink {

    private Class<? extends Component> view;
    private SvgIcon menuItemIcon = new SvgIcon();

    private final Paragraph menuItemName = new Paragraph();

    public SideNavigationMenuItem(RoadmapIcon icon, String text, Class<? extends Component> navigationTarget) {
        addClassName("side-navigation-menu-item");
        view = navigationTarget;
        setRoute(view);

        buildNavigationMenuItem(icon, text);
    }

    private void buildNavigationMenuItem(RoadmapIcon icon, String text) {
        menuItemIcon = icon.create();
        menuItemIcon.addClassName("menu-item-icon");

        menuItemName.addClassName("menu-item-name");
        menuItemName.setText(text);

        add(menuItemIcon, menuItemName);
    }
}
