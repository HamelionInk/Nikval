package com.nikitin.roadmaps.views;

import com.nikitin.roadmaps.exception.VaadinExceptionHandler;
import com.nikitin.roadmaps.views.helloworld.HelloWorldView;
import com.nikitin.roadmaps.views.homepage.HomePageView;
import com.nikitin.roadmaps.views.profile.ProfileView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

public class MainLayout extends AppLayout implements LocaleChangeObserver {

    private static final String APPLICATION_NAME_KEY = "main.layout.applicationName";
    private static final String SIDE_NAVIGATION_GENERAL_KEY = "main.layout.generalSideNavigation";
    private static final String SIDE_NAVIGATION_ROADMAPS_KEY = "main.layout.roadmapsSideNavigation";
    private static final String SIDE_NAV_ITEM_PROFILE_KEY = "main.layout.profileNavItem";
    private static final String SIDE_NAV_ITEM_HOME_PAGE_KEY = "main.layout.homePageNavItem";
    private static final String SIDE_NAV_ITEM_LOGIN_KEY = "main.layout.loginNavItem";
    private static final String SIDE_NAV_ITEM_SIGN_UP_KEY = "main.layout.signUpNavItem";
    private static final String LOCALE_SAVED_KEY = "main.layout.localeSaved";

    private final I18NProvider i18NProvider;

    private final ComboBox<Locale> localeChangeComboBox = new ComboBox<>();

    private final H1 applicationNameText;

    private final SideNav roadmapsSideNavigation;
    private final SideNav generalSideNavigation;

    private final SideNavItem homePageNavItem;
    private final SideNavItem profileNavItem;

    public MainLayout(@Autowired I18NProvider i18NProvider) {
        applicationNameText = new H1(getTranslation(APPLICATION_NAME_KEY));

        roadmapsSideNavigation = new SideNav(getTranslation(SIDE_NAVIGATION_ROADMAPS_KEY));
        generalSideNavigation = new SideNav(getTranslation(SIDE_NAVIGATION_GENERAL_KEY));

        homePageNavItem = new SideNavItem(getTranslation(SIDE_NAV_ITEM_HOME_PAGE_KEY), HomePageView.class, VaadinIcon.HOME.create());
        profileNavItem = new SideNavItem(getTranslation(SIDE_NAV_ITEM_PROFILE_KEY), ProfileView.class, VaadinIcon.USER_CARD.create());

        this.i18NProvider = i18NProvider;

        buildNavigationSideBar();
    }

    private void buildNavigationSideBar() {
        applicationNameText.addClassNames("applicationNameText");

        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.addClassNames("drawerToggle");

        generalSideNavigation.setCollapsible(true);

        generalSideNavigation.addItem(
                homePageNavItem,
                profileNavItem);


        roadmapsSideNavigation.setCollapsible(true);
        roadmapsSideNavigation.addItem(
                new SideNavItem("Java backend", HelloWorldView.class, VaadinIcon.SITEMAP.create()));

        localeChangeComboBox.addClassNames("localeChangeComboBox");
        localeChangeComboBox.setItems(i18NProvider.getProvidedLocales());
        localeChangeComboBox.setItemLabelGenerator(generator -> getTranslation(generator.getLanguage()));
        localeChangeComboBox.setValue(UI.getCurrent().getLocale());
        localeChangeComboBox.addValueChangeListener(event -> saveLocalPreference(event.getValue()));

        Image logo = new Image("images/logo-application.png", "placeholder plant");
        logo.addClassName("logo");

        addToNavbar(drawerToggle, logo, applicationNameText, localeChangeComboBox);
        addToDrawer(generalSideNavigation, roadmapsSideNavigation);
    }

    private void saveLocalPreference(Locale locale) {
        getUI().ifPresent(UI -> UI.setLocale(locale));
        localeChangeComboBox.setItemLabelGenerator(i -> getTranslation(i.getLanguage()));
        Notification.show(getTranslation(LOCALE_SAVED_KEY))
                .setPosition(Notification.Position.TOP_CENTER);
    }


    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        roadmapsSideNavigation.setLabel(getTranslation(SIDE_NAVIGATION_ROADMAPS_KEY));
        generalSideNavigation.setLabel(getTranslation(SIDE_NAVIGATION_GENERAL_KEY));
        homePageNavItem.setLabel(getTranslation(SIDE_NAV_ITEM_HOME_PAGE_KEY));
        profileNavItem.setLabel(getTranslation(SIDE_NAV_ITEM_PROFILE_KEY));
        applicationNameText.setText(getTranslation(APPLICATION_NAME_KEY));
    }
}
