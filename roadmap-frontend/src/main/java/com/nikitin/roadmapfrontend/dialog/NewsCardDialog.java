package com.nikitin.roadmapfrontend.dialog;

import com.nikitin.roadmapfrontend.component.ImageUpload;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class NewsCardDialog<T extends View> extends MainDialog<T> {

    private VerticalLayout newsCardBodyLayout = new VerticalLayout();
    private HorizontalLayout uploadLayout = new HorizontalLayout();
    private HorizontalLayout dateTimePickerLayout = new HorizontalLayout();
    private ImageUpload imageUpload = new ImageUpload();
    private Image newsCardImage = new Image();
    private TimePicker timePicker = new TimePicker();
    private DatePicker datePicker = new DatePicker();
    private TextField titleInput = new TextField();
    private TextArea descriptionInput = new TextArea();
    private Button actionButton = new Button();

    public NewsCardDialog(T view) {
        super(view);

        buildBody();
    }

    private void buildBody() {
        newsCardBodyLayout.addClassName("news-card-dialog-body-layout");
        dateTimePickerLayout.addClassName("news-card-dialog-picker-layout");
        timePicker.addClassName("news-card-time-picker");
        datePicker.addClassName("news-card-date-picker");
        uploadLayout.addClassName("news-card-dialog-upload-layout");
        newsCardImage.addClassName("news-card-dialog-image");
        titleInput.addClassName("news-card-dialog-title-input");
        descriptionInput.addClassName("news-card-dialog-description-input");
        actionButton.addClassName("news-card-dialog-action-button");

        titleInput.setLabel("Заголовок");
        descriptionInput.setLabel("Описание");
        datePicker.setLabel("Дата");
        timePicker.setLabel("Время");

        datePicker.setValue(LocalDate.now());
        timePicker.setValue(LocalTime.now());

        imageUpload.addImageUploadSuccessListener(event -> {
            try {
                newsCardImage.setSrc(imageUpload.encodeImage());
            } catch (IOException e) {
                //todo use notification show
                throw new RuntimeException(e);
            }
        });

        uploadLayout.add(imageUpload, newsCardImage);
        dateTimePickerLayout.add(datePicker, timePicker);

        newsCardBodyLayout.add(uploadLayout, dateTimePickerLayout, titleInput, descriptionInput, actionButton);

        setBodyLayout(newsCardBodyLayout);
    }

    public void setActionButtonName(String text) {
        actionButton.setText(text);
    }

    public void addActionButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        actionButton.addClickListener(listener);
    }

    public String getTitleInputValue() {
        return titleInput.getValue();
    }

    public void setTitleInputValue(String value) {
        titleInput.setValue(value);
    }

    public String getDescriptionInputValue() {
        return descriptionInput.getValue();
    }

    public void setDescriptionInputValue(String value) {
        descriptionInput.setValue(value);
    }

    public String getNewsCardImageSrc() {
        return newsCardImage.getSrc();
    }

    public void setNewsCardImageSrc(String src) {
        newsCardImage.setSrc(src);
    }

    public void setDateTime(LocalDateTime localDateTime) {
        datePicker.setValue(LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth()));
        timePicker.setValue(LocalTime.of(localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()));
    }

    public LocalDateTime getDateTimeValue() {
        return LocalDateTime.of(datePicker.getValue(), timePicker.getValue());
    }
}
