package com.nikitin.roadmaps.views.profile.layout;

import com.nikitin.roadmaps.views.profile.button.UserInfoEditButton;
import com.nikitin.roadmaps.views.profile.dialog.AvatarEditDialog;
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
import com.vaadin.flow.theme.lumo.LumoIcon;
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
    private Button editAvatarButton;
    private UserInfoEditButton userInfoEditButton;

    private AvatarEditDialog avatarEditDialog;

    public HeaderProfileLayout() {
        addClassName("header_profile_layout");

        avatarEditDialog = new AvatarEditDialog();

        var avatarDiv = new Div();
        avatarDiv.addClassName("avatar_div");

        userAvatar = new Avatar();
        userAvatar.addClassName("user_avatar");
        userAvatar.addThemeVariants(AvatarVariant.LUMO_XLARGE);

        editAvatarButton = new Button();
        editAvatarButton.addClassName("edit_avatar_button");
        editAvatarButton.setIcon(LumoIcon.EDIT.create());
        editAvatarButton.addClickListener(event -> avatarEditDialog.open());

        avatarDiv.add(userAvatar, editAvatarButton);

        userFullNameH3 = new H3("Михаил Никитин");
        userFullNameH3.addClassName("user_full_name_h3");

        userLoginDateH4 = new H4("2023-08-20 : 10:00:23");
        userLoginDateH4.addClassName("user_login_date_h4");

        add(avatarDiv, userFullNameH3, userLoginDateH4, buildButtonHeaderDiv());
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
