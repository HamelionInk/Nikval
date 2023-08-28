package com.nikitin.roadmaps.views.profile.layout;

import com.nikitin.roadmaps.views.profile.button.UserInfoEditButton;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.server.VaadinServletRequest;
import jakarta.servlet.ServletException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class HeaderProfileLayout extends HorizontalLayout implements LocaleChangeObserver {

    private Avatar userAvatar;
    private H3 userFullNameH3;
    private H4 userLoginDateH4;
    private Button logoutButton;
    private UserInfoEditButton userInfoEditButton;

    public HeaderProfileLayout() {
        addClassName("header_profile_layout");

        userAvatar = new Avatar();
        userAvatar.addThemeVariants(AvatarVariant.LUMO_XLARGE);
        userAvatar.addClassName("user_avatar");

        userFullNameH3 = new H3("Михаил Никитин");
        userFullNameH3.addClassName("user_full_name_h3");

        userLoginDateH4 = new H4("2023-08-20 : 10:00:23");
        userLoginDateH4.addClassName("user_login_date_h4");

        add(userAvatar, userFullNameH3, userLoginDateH4, buildButtonHeaderDiv());
    }

    private Div buildButtonHeaderDiv() {
        logoutButton = new Button("Выход из аккаунта", VaadinIcon.SIGN_OUT.create());
        logoutButton.addClickListener(event -> logoutEvent());
        logoutButton.addClassName("logout_button");

        userInfoEditButton = new UserInfoEditButton("Редактировать", VaadinIcon.EDIT.create());
        userInfoEditButton.addClassName("user_info_edit_button");

        Div buttonHeaderDiv = new Div();
        buttonHeaderDiv.addClassName("button_header_div");
        buttonHeaderDiv.add(userInfoEditButton, logoutButton);

        return buttonHeaderDiv;
    }

    private void logoutEvent() {
        try {
            VaadinServletRequest.getCurrent().logout();
        } catch (ServletException e) {
            log.info("Logout failed");
        }
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }


}
