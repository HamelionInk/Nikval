package com.nikitin.roadmapfrontend.component.dialog.newscard;

import com.nikitin.roadmapfrontend.component.ImageUpload;
import com.nikitin.roadmapfrontend.component.dialog.AbstractDialog;
import com.nikitin.roadmapfrontend.icon.RoadmapImage;
import com.nikitin.roadmapfrontend.utils.constants.DialogNameConstant;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.nikitin.roadmapfrontend.utils.editor.TextEditorBuilder;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.wontlost.ckeditor.VaadinCKEditor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class NewsCardCreateDialog extends AbstractDialog {

	private HorizontalLayout uploadLayout = new HorizontalLayout();
	private HorizontalLayout dateTimePickerLayout = new HorizontalLayout();
	private ImageUpload upload = new ImageUpload();
	private Image image = new Image();
	private TimePicker timePicker = new TimePicker(DialogNameConstant.TIME_PICKER_NAME, LocalTime.now());
	private DatePicker datePicker = new DatePicker(DialogNameConstant.DATE_PICKER_NAME, LocalDate.now());
	private TextField title = new TextField(DialogNameConstant.TITLE_NAME);
	private VaadinCKEditor description = TextEditorBuilder.getClassicCKEditor(null);

	public NewsCardCreateDialog() {

		buildComponent();
	}

	@Override
	public void buildComponent() {
		super.buildComponent();

		setHeaderTitle(DialogNameConstant.NEWS_CARD_CREATE_HEADER_TITLE);
		actionButton.setText(DialogNameConstant.NEWS_CARD_CREATE_ACTION_BUTTON_NAME);
		RoadmapImage.getImageUrl(RoadmapImage.CUSTOM_NEWS_CARD)
				.ifPresent(url -> image.setSrc(url));

		upload.addImageUploadSuccessListener(event -> {
			try {
				image.setSrc(upload.encodeImage());
			} catch (IOException e) {
				//todo use notification show
				throw new RuntimeException(e);
			}
		});

		uploadLayout.addClassNames(StyleClassConstant.FULL_WIDTH, StyleClassConstant.NEWS_CARD_UPLOAD_LAYOUT);
		dateTimePickerLayout.addClassName(StyleClassConstant.FULL_WIDTH);
		timePicker.addClassName(StyleClassConstant.FULL_WIDTH);
		datePicker.addClassName(StyleClassConstant.FULL_WIDTH);
		image.addClassName("news-card-dialog-image");
		title.addClassName(StyleClassConstant.FULL_WIDTH);
		description.addClassName(StyleClassConstant.FULL_WIDTH);
		actionButton.addClassName(StyleClassConstant.DIALOG_BLUE_BUTTON);
		addClassName(StyleClassConstant.NEWS_CARD_LAYOUT);

		uploadLayout.add(upload, image);
		dateTimePickerLayout.add(datePicker, timePicker);
		add(uploadLayout, dateTimePickerLayout, title, description);
	}

	public String getTitleInputValue() {
		return title.getValue();
	}

	public String getDescriptionInputValue() {
		return description.getValue();
	}

	public String getNewsCardImageSrc() {
		return image.getSrc();
	}

	public LocalDateTime getDateTimeValue() {
		return LocalDateTime.of(datePicker.getValue(), timePicker.getValue());
	}
}
