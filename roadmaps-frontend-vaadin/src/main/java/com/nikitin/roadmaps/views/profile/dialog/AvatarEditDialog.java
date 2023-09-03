package com.nikitin.roadmaps.views.profile.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class AvatarEditDialog extends Dialog implements LocaleChangeObserver {

    private FileBuffer fileBuffer;
    private Upload avatarUpload;
    private Button closeButton;
    private Paragraph infoTextParagraph;

    public AvatarEditDialog() {
        addClassName("avatar_edit_dialog");

        closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        closeButton.addClassName("close_button");
        closeButton.addClickListener(event -> close());

        infoTextParagraph = new Paragraph("Поддерживаемые форматы файла JPG (.jpg), PNG (.png)");
        infoTextParagraph.addClassName("info_text_h5");

        int maxFileSizeInBytes = 10 * 1024 * 1024; // 10MB
        fileBuffer = new FileBuffer();
        avatarUpload = new Upload(fileBuffer);
        avatarUpload.addClassName("avatar_upload");
        avatarUpload.setAcceptedFileTypes(".png", ".jpg");
        avatarUpload.setMaxFiles(1);
        avatarUpload.setMaxFileSize(maxFileSizeInBytes);

        setHeaderTitle("Загрузка изображения");
        getHeader().add(closeButton);

        add(infoTextParagraph, avatarUpload);
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
