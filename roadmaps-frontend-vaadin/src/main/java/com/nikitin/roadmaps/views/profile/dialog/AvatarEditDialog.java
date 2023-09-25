package com.nikitin.roadmaps.views.profile.dialog;

import com.nikitin.roadmaps.client.ProfileClient;
import com.nikitin.roadmaps.exception.UploadAvatarException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
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

        buildCloseButton();
        buildInfoTextParagraph();
        buildAvatarUpload();

        setHeaderTitle("Загрузка изображения");
        getHeader().add(closeButton);

        add(infoTextParagraph, avatarUpload);
    }

    private void buildAvatarUpload() {
        int maxFileSizeInBytes = 10 * 1024 * 1024; // 10MB
        var uploadI18N = new UploadI18N();
        uploadI18N.setError(new UploadI18N.Error());

        fileBuffer = new FileBuffer();

        avatarUpload = new Upload(fileBuffer);
        avatarUpload.addClassName("avatar_upload");
        avatarUpload.setAcceptedFileTypes(".png", ".jpg");
        avatarUpload.setMaxFiles(1);
        avatarUpload.setMaxFileSize(maxFileSizeInBytes);
        avatarUpload.addFileRejectedListener(event -> {
            throw new UploadAvatarException(event.getErrorMessage());
        });
        avatarUpload.addFailedListener(event -> {
            throw new UploadAvatarException(event.getReason().getMessage());
        });

        avatarUpload.setI18n(uploadI18N);
        avatarUpload.getI18n().getError().setFileIsTooBig("Загружаемый файл не может быть больше 10МБ");
        avatarUpload.getI18n().getError().setIncorrectFileType("Неверный формат файла");
        avatarUpload.getI18n().getError().setTooManyFiles("Одновременно можно загружать только один файл");
    }

    private void buildInfoTextParagraph() {
        infoTextParagraph = new Paragraph("Поддерживаемые форматы файла JPG (.jpg), PNG (.png)");
        infoTextParagraph.addClassName("info_text_h5");
    }

    private void buildCloseButton() {
        closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        closeButton.addClassName("close_button");
        closeButton.addClickListener(event -> close());
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
