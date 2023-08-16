package com.nikitin.roadmaps.views;

import com.nikitin.roadmaps.views.helloworld.HelloWorldView;
import com.nikitin.roadmaps.views.homepage.HomePageView;
import com.nikitin.roadmaps.views.login.LoginView;
import com.nikitin.roadmaps.views.profile.ProfileView;
import com.nikitin.roadmaps.views.signup.SignUpView;
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
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.sso.starter.SingleSignOnProperties;
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

    private final AuthenticationContext authenticationContext;
    private final SingleSignOnProperties ssoProperties;
    private final I18NProvider i18NProvider;

    private final ComboBox<Locale> localeChangeComboBox = new ComboBox<>();

    private final H1 applicationNameText;

    private final SideNav roadmapsSideNavigation;
    private final SideNav generalSideNavigation;

    private final SideNavItem homePageNavItem;
    private final SideNavItem profileNavItem;
    private final SideNavItem loginNavItem;
    private final SideNavItem signUpNavItem;

    public MainLayout(@Autowired I18NProvider i18NProvider, AuthenticationContext authenticationContext,
                      SingleSignOnProperties ssoProperties) {
        applicationNameText = new H1(getTranslation(APPLICATION_NAME_KEY));

        roadmapsSideNavigation = new SideNav(getTranslation(SIDE_NAVIGATION_ROADMAPS_KEY));
        generalSideNavigation= new SideNav(getTranslation(SIDE_NAVIGATION_GENERAL_KEY));

        homePageNavItem = new SideNavItem(getTranslation(SIDE_NAV_ITEM_HOME_PAGE_KEY), HomePageView.class, VaadinIcon.HOME.create());
        profileNavItem = new SideNavItem(getTranslation(SIDE_NAV_ITEM_PROFILE_KEY), ProfileView.class, VaadinIcon.USER_CARD.create());
        loginNavItem = new SideNavItem(getTranslation(SIDE_NAV_ITEM_LOGIN_KEY), LoginView.class, VaadinIcon.SIGN_IN.create());
        signUpNavItem = new SideNavItem(getTranslation(SIDE_NAV_ITEM_SIGN_UP_KEY), SignUpView.class, VaadinIcon.PLUS.create());

        this.i18NProvider = i18NProvider;
        this.authenticationContext = authenticationContext;
        this.ssoProperties = ssoProperties;
        buildNavigationSideBar();
    }

    private void buildNavigationSideBar() {
        applicationNameText.addClassNames("applicationNameText");

        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.addClassNames("drawerToggle");

        generalSideNavigation.setCollapsible(true);

        if (authenticationContext.isAuthenticated()) {
            generalSideNavigation.addItem(
                    homePageNavItem,
                    profileNavItem);
        } else {
            generalSideNavigation.addItem(
                    loginNavItem,
                    signUpNavItem);
        }

        roadmapsSideNavigation.setCollapsible(true);
        roadmapsSideNavigation.addItem(
                new SideNavItem("Java backend", HelloWorldView.class, VaadinIcon.SITEMAP.create()));

        localeChangeComboBox.addClassNames("localeChangeComboBox");
        localeChangeComboBox.setItems(i18NProvider.getProvidedLocales());
        localeChangeComboBox.setItemLabelGenerator(generator -> getTranslation(generator.getLanguage()));
        localeChangeComboBox.setValue(UI.getCurrent().getLocale());
        localeChangeComboBox.addValueChangeListener(event ->saveLocalPreference(event.getValue()));

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
        loginNavItem.setLabel(getTranslation(SIDE_NAV_ITEM_LOGIN_KEY));
        signUpNavItem.setLabel(getTranslation(SIDE_NAV_ITEM_SIGN_UP_KEY));
        applicationNameText.setText(getTranslation(APPLICATION_NAME_KEY));
    }
}
