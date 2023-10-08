package com.nikitin.roadmaps.views;

import com.nikitin.roadmaps.client.RoadmapClient;
import com.nikitin.roadmaps.dto.response.pageable.PageableRoadmapResponseDto;
import com.nikitin.roadmaps.util.RestUtils;
import com.nikitin.roadmaps.views.profile.ProfileProgressView;
import com.nikitin.roadmaps.views.profile.ProfileView;
import com.nikitin.roadmaps.views.roadmaps.RoadmapInfoView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;
import java.util.stream.Stream;
@Slf4j
@Getter
@Setter
public class MainLayout extends AppLayout implements LocaleChangeObserver, AfterNavigationObserver {

    private static final String APPLICATION_NAME_KEY = "main.layout.applicationName";
    private static final String SIDE_NAVIGATION_GENERAL_KEY = "main.layout.generalSideNavigation";
    private static final String SIDE_NAVIGATION_ROADMAPS_KEY = "main.layout.roadmapsSideNavigation";
    private static final String SIDE_NAV_ITEM_PROFILE_KEY = "main.layout.profileNavItem";
    private static final String SIDE_NAV_ITEM_HOME_PAGE_KEY = "main.layout.homePageNavItem";
    private static final String LOCALE_SAVED_KEY = "main.layout.localeSaved";

    private final I18NProvider i18NProvider;
    private final ComboBox<Locale> localeChange = new ComboBox<>();
    private final DrawerToggle drawerToggle = new DrawerToggle();
    private final Tabs navigationBarTabs = new Tabs();
    private final Tab home = new Tab();
    private final Tab profile = new Tab();
    private final Tab roadmaps = new Tab();
    private final Tab tutorial = new Tab();
    private final SideNavItem profileSideNavItem = new SideNavItem("Information", ProfileView.class);
    private final SideNavItem profileSideNavItem2 = new SideNavItem("Progress", ProfileProgressView.class);
    private final SideNavItem roadmapSideNavItem = new SideNavItem("My Roadmaps", RoadmapInfoView.class);
    private final SideNavItem roadmapSideNavItem2 = new SideNavItem("Test Info 2");

    private RoadmapClient roadmapClient;

    public MainLayout(@Autowired I18NProvider i18NProvider, @Autowired RoadmapClient roadmapClient) {
        this.roadmapClient = roadmapClient;
        this.i18NProvider = i18NProvider;

        buildNavigationHeaderBar();
        buildNavigationSideBar();

        addClassName("main_layout");
    }

    private void buildNavigationHeaderBar() {
        drawerToggle.addClassNames("drawer_toggle");

        localeChange.addClassNames("locale_change");
        localeChange.setItems(i18NProvider.getProvidedLocales());
        localeChange.setItemLabelGenerator(generator -> getTranslation(generator.getLanguage()));
        localeChange.setValue(UI.getCurrent().getLocale());
        localeChange.addValueChangeListener(event -> saveLocalPreference(event.getValue()));

        home.addClassName("main_tab");
        home.setLabel("Home");

        profile.addClassName("main_tab");
        profile.setLabel("Profile");

        roadmaps.addClassName("main_tab");
        roadmaps.setLabel("Roadmaps");

        tutorial.addClassName("main_tab");
        tutorial.setLabel("Guide");

        navigationBarTabs.addClassNames("main_navigation_bar_tabs");
        navigationBarTabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        navigationBarTabs.add(home, profile, roadmaps, tutorial);

        addToNavbar(drawerToggle, navigationBarTabs, localeChange);
    }

    private void buildNavigationSideBar() {
        Stream.of(profileSideNavItem, profileSideNavItem2, roadmapSideNavItem, roadmapSideNavItem2).forEach(this::addToDrawer);
    }

    private PageableRoadmapResponseDto getAllByProfileId(Long id) {
        var response = roadmapClient.getAllByProfileId(id, true);
        if (response.getStatusCode().is2xxSuccessful()) {
            return RestUtils.convertResponseToDto(response.getBody(), PageableRoadmapResponseDto.class);
        }
        return new PageableRoadmapResponseDto();
    }

    private void saveLocalPreference(Locale locale) {
        getUI().ifPresent(UI -> UI.setLocale(locale));
        localeChange.setItemLabelGenerator(i -> getTranslation(i.getLanguage()));
        Notification.show(getTranslation(LOCALE_SAVED_KEY))
                .setPosition(Notification.Position.TOP_CENTER);
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        Stream.of(profileSideNavItem, profileSideNavItem2, roadmapSideNavItem, roadmapSideNavItem2).forEach(item ->
                item.setVisible(false));

        if (afterNavigationEvent.getLocation().getPath().equals("profile")) {
            Stream.of(profileSideNavItem, profileSideNavItem2).forEach(item -> item.setVisible(true));
            navigationBarTabs.setSelectedTab(profile);
        }

        if (afterNavigationEvent.getLocation().getPath().equals("profile/progress")) {
            Stream.of(profileSideNavItem, profileSideNavItem2).forEach(item -> item.setVisible(true));
            navigationBarTabs.setSelectedTab(profile);
        }

        if (afterNavigationEvent.getLocation().getFirstSegment().equals("roadmaps")) {
            Stream.of(roadmapSideNavItem, roadmapSideNavItem2).forEach(item -> item.setVisible(true));
            navigationBarTabs.setSelectedTab(roadmaps);
        }

        if (afterNavigationEvent.getLocation().getPath().equals("guide")) {
            Stream.of(roadmapSideNavItem, roadmapSideNavItem2).forEach(item -> item.setVisible(true));
            navigationBarTabs.setSelectedTab(tutorial);
        }

        navigationBarTabs.addSelectedChangeListener(event -> {
            if (home.isSelected()) {
                UI.getCurrent().navigateToClient("http://localhost:8082");
            }
            if (profile.isSelected()) {
                UI.getCurrent().navigateToClient("http://localhost:8082/profile");
            }
            if (roadmaps.isSelected()) {
                UI.getCurrent().navigate("http://localhost:8082/roadmaps");
            }
            if (tutorial.isSelected()) {
                UI.getCurrent().navigateToClient("http://localhost:8082/guide");
            }
        });
    }
}
