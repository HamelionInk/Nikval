package com.nikitin.roadmapfrontend.component.dialog.roadmapcard;

import com.nikitin.roadmapfrontend.component.dialog.AbstractDialog;
import com.nikitin.roadmapfrontend.dto.response.RoadmapResponseDto;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.textfield.TextField;

public class RoadmapCardEditDialog extends AbstractDialog {

	private final RoadmapResponseDto roadmapResponseDto;

	private final TextField roadmapNameComponent = new TextField("Название");
	private final ToggleButton roadmapFavoriteComponent = new ToggleButton("Добавить в избранное");

	public RoadmapCardEditDialog(RoadmapResponseDto roadmapResponseDto) {
		this.roadmapResponseDto = roadmapResponseDto;

		buildComponent();
	}

	@Override
	public void buildComponent() {
		super.buildComponent();

		setHeaderTitle("Редактирование карты развития");

		actionButton.setText("Сохранить");
		actionButton.addClassName(StyleClassConstant.DIALOG_BLUE_BUTTON);
		roadmapFavoriteComponent.setValue(roadmapResponseDto.getFavorite());
		roadmapFavoriteComponent.addClassName(StyleClassConstant.ROADMAP_FAVORITE_COMPONENT);
		roadmapNameComponent.setValue(roadmapResponseDto.getName());
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
