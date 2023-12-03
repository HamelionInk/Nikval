package com.nikitin.roadmapfrontend.component;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.IOException;
import java.util.Arrays;

public class ImageUpload extends VerticalLayout {

    private final HorizontalLayout imageUploadHeader = new HorizontalLayout();
    private final Paragraph fileTypesName = new Paragraph();
    private final FileBuffer fileBuffer = new FileBuffer();
    private final Upload imageUpload = new Upload(fileBuffer);

    public ImageUpload() {
        String[] acceptedFileTypes = {".png", ".jpg"};

        configureFileTypes(acceptedFileTypes);
        configureMaxFiles(1);

        add(imageUploadHeader, imageUpload);
    }

    public ImageUpload(Integer maxFiles, String... fileTypes) {
        configureFileTypes(fileTypes);
        configureMaxFiles(maxFiles);

        add(imageUploadHeader, imageUpload);
    }

    private void configureFileTypes(String... fileTypes) {
        imageUpload.setAcceptedFileTypes(fileTypes);

        var stringBuilder = new StringBuilder("Допустимый формат:");
        Arrays.stream(fileTypes).forEach(type -> stringBuilder.append(" ").append(type));

        fileTypesName.setText(stringBuilder.toString());
        fileTypesName.addClassName("image-upload-file-types-name");
        imageUploadHeader.add(fileTypesName);
    }

    private void configureMaxFiles(Integer max) {
        imageUpload.setMaxFiles(max);
    }

    public void addImageUploadSuccessListener(ComponentEventListener<SucceededEvent> listener) {
        imageUpload.addSucceededListener(listener);
    }

    public String encodeImage() throws IOException {
        var imageData = fileBuffer.getInputStream().readAllBytes();
        return "data:" + fileBuffer.getFileData().getMimeType() + ";base64," + Base64.encodeBase64String(imageData);
    }
}
