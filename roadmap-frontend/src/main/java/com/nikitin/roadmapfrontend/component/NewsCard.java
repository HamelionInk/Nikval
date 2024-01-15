package com.nikitin.roadmapfrontend.component;

import com.nikitin.roadmapfrontend.client.NewsCardClient;
import com.nikitin.roadmapfrontend.configuration.security.SecurityService;
import com.nikitin.roadmapfrontend.dialog.NewsCardDialog;
import com.nikitin.roadmapfrontend.dto.request.NewsCardRequestDto;
import com.nikitin.roadmapfrontend.dto.response.NewsCardResponseDto;
import com.nikitin.roadmapfrontend.icon.RoadmapIcon;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Getter
@Setter
public class NewsCard<T extends View> extends VerticalLayout {

	private T view;
	private NewsCardResponseDto newsCardResponseDto;

	private HorizontalLayout newsCardHeader = new HorizontalLayout();
	private Paragraph cardName = new Paragraph();
	private Image cardImage = new Image();

	public NewsCard(NewsCardResponseDto newsCardResponseDto, T view) {
		this.view = view;
		this.newsCardResponseDto = newsCardResponseDto;

		build();
		buildForRoles();

		addClassName("news-card");
		addClickListener(event -> UI.getCurrent().navigate("/news/" + newsCardResponseDto.getId()));

		add(newsCardHeader, cardImage);
	}

	private void build() {
		newsCardHeader.addClassName("news-card-header");
		cardName.addClassName("card-name");
		cardImage.addClassName("news-card-image");

		cardName.setText(LocalDate.now() + " - " + newsCardResponseDto.getTitle());
		cardImage.setSrc(newsCardResponseDto.getImage());

		newsCardHeader.add(cardName);
	}

	private void buildForRoles() {
		if (SecurityService.getAuthorities().contains("ROLE_ADMIN")) {
			var newsCardDeleteButton = new Button("Удалить");
			newsCardDeleteButton.addClassName("news_card_delete");
			newsCardDeleteButton.addClickListener(event -> {
				view.getClient(NewsCardClient.class).deleteById(newsCardResponseDto.getId());
				view.refreshView();
			});

			var newsCardEditButton = new Button("Редактировать");
			newsCardEditButton.addClassName("news_card_delete");
			newsCardEditButton.addClickListener(event -> {
				var editNewsCardDialog = new NewsCardDialog<>(view);

				editNewsCardDialog.setTitleInputValue(newsCardResponseDto.getTitle());
				editNewsCardDialog.setDescriptionInputValue(newsCardResponseDto.getDescription());
				editNewsCardDialog.setNewsCardImageSrc(newsCardResponseDto.getImage());
				editNewsCardDialog.setDateTime(newsCardResponseDto.getCreatedAt());

				editNewsCardDialog.setHeaderName("Редактировать новость");
				editNewsCardDialog.setActionButtonName("Сохранить");
				editNewsCardDialog.addActionButtonClickListener(action -> {
					view.getClient(NewsCardClient.class).patch(newsCardResponseDto.getId(), NewsCardRequestDto.builder()
							.title(editNewsCardDialog.getTitleInputValue())
							.description(editNewsCardDialog.getDescriptionInputValue())
							.image(editNewsCardDialog.getNewsCardImageSrc())
							.createdAt(editNewsCardDialog.getDateTimeValue())
							.build());

					view.refreshView();
					editNewsCardDialog.close();
				});

				editNewsCardDialog.open();
			});

			var dropDownMenu = new DropDownMenu(RoadmapIcon.DROP_DOWN_VERTICAL, newsCardEditButton, newsCardDeleteButton);
			dropDownMenu.addClassName("news-card-drop-down-menu");
			dropDownMenu.getMenuBarIcon().addClassName("news-card-menu-bar-icon");
			dropDownMenu.getMenuItem().addClassName("news-card-menu-item");

			newsCardHeader.add(dropDownMenu);
		}
	}
}
