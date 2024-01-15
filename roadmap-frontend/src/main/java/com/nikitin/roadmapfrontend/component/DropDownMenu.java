package com.nikitin.roadmapfrontend.component;

import com.nikitin.roadmapfrontend.icon.RoadmapIcon;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class DropDownMenu extends MenuBar {

    private SvgIcon menuBarIcon;
    private MenuItem menuItem;

    public DropDownMenu(RoadmapIcon menuBarIcon, Component... components) {
        addClassName("drop-down-menu");
        this.menuBarIcon = menuBarIcon.create();
        menuItem = addItem(this.menuBarIcon);

        Arrays.stream(components).forEach(item -> {
            item.addClassName("drop-down-component");
            menuItem.getSubMenu().addItem(item);
        });
    }
}
