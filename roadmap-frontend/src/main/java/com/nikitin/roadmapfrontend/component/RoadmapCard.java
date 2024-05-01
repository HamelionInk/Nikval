package com.nikitin.roadmapfrontend.component;

import com.nikitin.roadmapfrontend.client.RoadmapClient;
import com.nikitin.roadmapfrontend.component.dialog.RoadmapCardEditDialog;
import com.nikitin.roadmapfrontend.dto.request.RoadmapRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapResponseDto;
import com.nikitin.roadmapfrontend.icon.RoadmapIcon;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class RoadmapCard<T extends View> extends VerticalLayout {

	private final T view;
	private final RoadmapResponseDto roadmapResponseDto;

	private final Div roadmapHeaderLayout = new Div();
	private final Paragraph roadmapName = new Paragraph();

	public RoadmapCard(RoadmapResponseDto roadmapResponseDto, T view) {
		this.roadmapResponseDto = roadmapResponseDto;
		this.view = view;
		addClassName("roadmap-card");

		if (roadmapResponseDto.getCustom()) {
			addClassName("custom-true");
		} else {
			addClassName("custom-false");
		}

		buildCard();
	}

	private void buildCard() {
		roadmapHeaderLayout.addClassName("roadmap-header-layout");
		roadmapName.addClassName("roadmap-name");
		roadmapName.setText(roadmapResponseDto.getName());

		roadmapHeaderLayout.add(buildFavoriteButton(), roadmapName, buildEditMenu());

		addClickListener(event -> UI.getCurrent().navigate("/roadmap/" + roadmapResponseDto.getId()));

		add(roadmapHeaderLayout);
	}

	private Button buildFavoriteButton() {
		var editFavoriteButton = new Button(buildFavoriteIcon(roadmapResponseDto.getFavorite()));
		editFavoriteButton.addClassName("edit-favorite-button");
		editFavoriteButton.addThemeVariants(ButtonVariant.LUMO_ICON);

		editFavoriteButton.addClickListener(event -> {
			view.getClient(RoadmapClient.class).patch(roadmapResponseDto.getId(), RoadmapRequestDto.builder()
					.favorite(!roadmapResponseDto.getFavorite())
					.build());
			view.refreshView();
		});

		return editFavoriteButton;
	}

	private SvgIcon buildFavoriteIcon(Boolean isFavorite) {
		var favoriteIcon = RoadmapIcon.FAVORITE_WHITELIST.create();
		favoriteIcon.addClassName("favorite-icon-card");
		favoriteIcon.setColor("white");

		if (isFavorite) {
			favoriteIcon.setColor("#f06a1d");
			return favoriteIcon;
		}

		return favoriteIcon;
	}

	private DropDownMenu buildEditMenu() {
		var editCardButton = new Button("Редактировать");
		var deleteCardButton = new Button("Удалить");

		editCardButton.addClickListener(event -> {
			var editRoadmapCardDialog = new RoadmapCardEditDialog(roadmapResponseDto);

			editRoadmapCardDialog.addActionButtonClickListener(action -> {
				view.getClient(RoadmapClient.class).patch(roadmapResponseDto.getId(), RoadmapRequestDto.builder()
						.favorite(editRoadmapCardDialog.getRoadmapFavoriteComponentValue())
						.name(editRoadmapCardDialog.getRoadmapNameComponentValue())
						.build());

				view.refreshView();
				editRoadmapCardDialog.close();
			});

			editRoadmapCardDialog.open();
		});

		deleteCardButton.addClickListener(event -> {
			view.getClient(RoadmapClient.class).deleteById(roadmapResponseDto.getId());
			view.refreshView();
		});

		var dropDownCardMenu = new DropDownMenu(RoadmapIcon.DROP_DOWN_VERTICAL, editCardButton, deleteCardButton);
		dropDownCardMenu.getMenuBarIcon().addClassName("roadmap-dropdown-card-menu-icon");
		dropDownCardMenu.addClassName("roadmap-dropdown-card-menu");

		return dropDownCardMenu;
	}
}
