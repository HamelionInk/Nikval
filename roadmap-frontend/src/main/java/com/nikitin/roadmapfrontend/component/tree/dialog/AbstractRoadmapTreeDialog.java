package com.nikitin.roadmapfrontend.component.tree.dialog;

import com.nikitin.roadmapfrontend.utils.enums.RoadmapTreeDialogType;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;

public abstract class AbstractRoadmapTreeDialog<T, V extends View> extends Dialog {

	protected Button closeButton;
	protected Button actionButton;

	public AbstractRoadmapTreeDialog(RoadmapTreeDialogType type, T data, V view, Component mainComponent) {

		buildComponent(type, data, view, mainComponent);
	}

	private void buildComponent(RoadmapTreeDialogType type, T data, V view, Component mainComponent) {
		closeButton = new Button("Закрыть");
		closeButton.addClickListener(event -> close());
		closeButton.addClassName("background-363636");

		actionButton = new Button("Кнопка");
		actionButton.addClassName("background-363636");

		getFooter().add(actionButton, closeButton);
		addClassName("roadmap-tree-dialog");
		add(buildDialogLayout(type, data, view, mainComponent));
	}

	abstract Component buildDialogLayout(RoadmapTreeDialogType type, T data, V view, Component mainComponent);
}
