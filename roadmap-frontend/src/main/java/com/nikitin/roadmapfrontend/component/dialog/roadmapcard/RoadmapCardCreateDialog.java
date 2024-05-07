package com.nikitin.roadmapfrontend.component.dialog.roadmapcard;

import com.nikitin.roadmapfrontend.component.dialog.AbstractDialog;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.textfield.TextField;

public class RoadmapCardCreateDialog extends AbstractDialog {

	private final TextField roadmapNameComponent = new TextField("Название");
	private final ToggleButton roadmapFavoriteComponent = new ToggleButton("Добавить в избранное");

	public RoadmapCardCreateDialog() {
		buildComponent();
	}

	@Override
	public void buildComponent() {
		super.buildComponent();

		setHeaderTitle("Создание карты развития");

		actionButton.setText("Создать");
		actionButton.addClassName(StyleClassConstant.DIALOG_BLUE_BUTTON);
		roadmapFavoriteComponent.addClassName(StyleClassConstant.ROADMAP_FAVORITE_COMPONENT);
		roadmapNameComponent.addClassName(StyleClassConstant.FULL_WIDTH);

		add(roadmapFavoriteComponent, roadmapNameComponent);
	}

	public String getRoadmapNameComponentValue() {
		return roadmapNameComponent.getValue();
	}

	public Boolean getRoadmapFavoriteComponentValue() {
		return roadmapFavoriteComponent.getValue();
	}
}
