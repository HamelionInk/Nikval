package com.nikitin.roadmapfrontend.dialog;

import com.nikitin.roadmapfrontend.client.ProfileClient;
import com.nikitin.roadmapfrontend.component.ImageUpload;
import com.nikitin.roadmapfrontend.utils.enums.VaadinSessionAttribute;
import com.nikitin.roadmapfrontend.dto.request.ProfileRequestDto;
import com.nikitin.roadmapfrontend.utils.SessionHelper;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class ProfileAvatarDialog<T extends View> extends MainDialog<T> {

    private final VerticalLayout profileAvatarLayout = new VerticalLayout();
    private final ImageUpload avatarUpload = new ImageUpload();
    public ProfileAvatarDialog(T view) {
        super(view);
        setHeaderName("Загрузить аватар");

        buildBody();
    }

    private void buildBody() {
        avatarUpload.addImageUploadSuccessListener(event -> {
            try {
                getView().getClient(ProfileClient.class)
                        .patch((Long) SessionHelper.getSessionAttribute(VaadinSessionAttribute.PROFILE_ID), ProfileRequestDto.builder()
                                .picture(avatarUpload.encodeImage())
                                .build()
                        );

                getView().refreshView();
                close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        profileAvatarLayout.addClassName("profile-avatar-layout");
        profileAvatarLayout.add(avatarUpload);

        setBodyLayout(profileAvatarLayout);
    }
}
