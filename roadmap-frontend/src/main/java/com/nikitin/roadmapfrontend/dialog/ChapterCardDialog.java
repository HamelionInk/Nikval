package com.nikitin.roadmapfrontend.dialog;

import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChapterCardDialog<T extends View> extends MainDialog<T> {

	private VerticalLayout chapterCardLayout;
	private TextField chapterName;
	private Button actionButton;

	public ChapterCardDialog(T view) {
		super(view);

		buildDialogBody();
	}

	private void buildDialogBody() {
		chapterCardLayout = new VerticalLayout();
		chapterCardLayout.addClassName("chapter-card-layout");

		chapterName = new TextField("Наименование раздела");
		chapterName.addClassName("chapter-name");

		actionButton = new Button();
		actionButton.addClassName("chapter-action-button");

		chapterCardLayout.add(chapterName, actionButton);

		setBodyLayout(chapterCardLayout);
	}

	public void addActionButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		actionButton.addClickListener(listener);
	}

	public void setActionButtonName(String value) {
		actionButton.setText(value);
	}

	public void setChapterName(String value) {
		chapterName.setValue(value);
	}

	public String getChapterName() {
		return chapterName.getValue();
	}
}
