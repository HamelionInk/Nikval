package com.nikitin.roadmapfrontend.component.sidebar;

import com.nikitin.roadmapfrontend.component.sidebar.menu.SideNavigationMenuItem;
import com.nikitin.roadmapfrontend.dto.response.ProfileResponseDto;
import com.nikitin.roadmapfrontend.icon.RoadmapIcon;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.server.VaadinServletRequest;
import jakarta.servlet.ServletException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class SideNavigationBar extends Div implements AfterNavigationObserver {

    private static final String OPEN_NAVIGATION_SIDE_BAR_CLASS_NAME = "active-true";
    private static final String CLOSE_NAVIGATION_SIDE_BAR_CLASS_NAME = "active-false";
    private static final String SELECTED_NAVIGATION_MENU_ITEM_CLASS_NAME = "navigation-item-selected";

    private Boolean isOpened = false;

    private final Button sideNavigationToggle = new Button();
    private final Button sideNavigationLogout = new Button();
    private final Div sideNavigationMenu = new Div();

    private ProfileResponseDto profileResponseDto;

    public SideNavigationBar(ProfileResponseDto profileResponseDto, RoadmapIcon toggleIcon,
                             List<SideNavigationMenuItem> sideNavigationMenuItems) {
        this.profileResponseDto = profileResponseDto;

        addClassName("side-navigation-bar");

        buildSideNavigationToggle(toggleIcon);
        buildSideNavigationMenu(sideNavigationMenuItems);
        buildSideNavigationLogout();

        add(sideNavigationToggle, sideNavigationMenu, sideNavigationLogout);
    }

    private void buildSideNavigationToggle(RoadmapIcon icon) {
        sideNavigationToggle.addClassName("side-navigation-toggle");
        sideNavigationToggle.setIcon(icon.create());
        sideNavigationToggle.addThemeVariants(ButtonVariant.LUMO_ICON);
        setVisibleToggleText(profileResponseDto.getFullName());

        sideNavigationToggle.addClickListener(event -> {
            if (isOpened) {
                close();
            } else {
                open();
            }
        });
    }

    private void buildSideNavigationMenu(List<SideNavigationMenuItem> sideNavigationMenuItems) {
        sideNavigationMenu.addClassName("side-navigation-menu");

        sideNavigationMenuItems.forEach(sideNavigationMenu::add);
        setVisibleMenuItemsName();
    }

    private void buildSideNavigationLogout() {
        sideNavigationLogout.addClassName("side-navigation-logout");
        sideNavigationLogout.setIcon(RoadmapIcon.EXIT.create());

        sideNavigationLogout.addClickListener(event -> {
            try {
                VaadinServletRequest.getCurrent().logout();
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        });

        setVisibleLogoutText("Выйти");
    }

    public void close() {
        removeClassName(OPEN_NAVIGATION_SIDE_BAR_CLASS_NAME);
        addClassName(CLOSE_NAVIGATION_SIDE_BAR_CLASS_NAME);

        isOpened = false;
        setVisibleToggleText(profileResponseDto.getFullName());
        setVisibleMenuItemsName();
        setVisibleLogoutText("Выйти");
    }

    public void open() {
        removeClassName(CLOSE_NAVIGATION_SIDE_BAR_CLASS_NAME);
        addClassName(OPEN_NAVIGATION_SIDE_BAR_CLASS_NAME);

        isOpened = true;
        setVisibleToggleText(profileResponseDto.getFullName());
        setVisibleMenuItemsName();
        setVisibleLogoutText("Выйти");
    }

    public void setVisibleMenuItemsName() {
        for (var count = 0; count < sideNavigationMenu.getComponentCount(); count++) {
            var sideNavigationMenuItem = sideNavigationMenu.getComponentAt(count);

            if (sideNavigationMenuItem instanceof SideNavigationMenuItem) {
                ((SideNavigationMenuItem) sideNavigationMenuItem).getMenuItemName().setVisible(isOpened);

                Tooltip.forComponent(((SideNavigationMenuItem) sideNavigationMenuItem).getMenuItemIcon())
                        .withText(((SideNavigationMenuItem) sideNavigationMenuItem).getMenuItemName().getText())
                        .withPosition(Tooltip.TooltipPosition.START)
                        .withManual(isOpened)
                        .setHideDelay(20);
            }
        }
    }

    public void setVisibleLogoutText(String logoutText) {
        Tooltip.forComponent(sideNavigationLogout)
                .withText(logoutText)
                .withPosition(Tooltip.TooltipPosition.START)
                .withManual(isOpened)
                .setHideDelay(20);

        if (isOpened) {
            sideNavigationLogout.setText(logoutText);
        } else {
            sideNavigationLogout.setText(StringUtils.EMPTY);
        }
    }

    public void setVisibleToggleText(String toggleText) {
        if (isOpened) {
            sideNavigationToggle.setText(toggleText);
        } else {
            sideNavigationToggle.setText(StringUtils.EMPTY);
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        var afterNavigationTarget = afterNavigationEvent.getLocation().getPath();

        for (var count = 0; count < sideNavigationMenu.getComponentCount(); count++) {
            var sideNavigationMenuItem = sideNavigationMenu.getComponentAt(count);

            if (sideNavigationMenuItem instanceof SideNavigationMenuItem) {
                var navigationTarget = ((SideNavigationMenuItem) sideNavigationMenuItem).getView();

                ComponentUtil.getRouter(this).getRegistry().getTargetUrl(navigationTarget).ifPresent(nav -> {
                    if (afterNavigationTarget.equalsIgnoreCase(nav)) {
                        sideNavigationMenuItem.addClassName(SELECTED_NAVIGATION_MENU_ITEM_CLASS_NAME);
                    } else {
                        sideNavigationMenuItem.removeClassName(SELECTED_NAVIGATION_MENU_ITEM_CLASS_NAME);
                    }
                });
            }
        }
    }
}
