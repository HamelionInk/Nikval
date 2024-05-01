package com.nikitin.roadmapfrontend.view;

import com.nikitin.roadmapfrontend.client.RoadmapClient;
import com.nikitin.roadmapfrontend.component.RoadmapCard;
import com.nikitin.roadmapfrontend.component.ViewHeader;
import com.nikitin.roadmapfrontend.component.dialog.RoadmapsCardCreateDialog;
import com.nikitin.roadmapfrontend.utils.enums.RoadmapType;
import com.nikitin.roadmapfrontend.utils.enums.VaadinSessionAttribute;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapFilter;
import com.nikitin.roadmapfrontend.dto.request.RoadmapRequestDto;
import com.nikitin.roadmapfrontend.utils.SessionHelper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RolesAllowed(value = {"ROLE_USER", "ROLE_ADMIN"})
@Route(value = "roadmaps", layout = MainView.class)
public class RoadmapsView extends VerticalLayout implements View {

	private final RoadmapClient roadmapClient;

	private final ComboBox<RoadmapType> changeTypeRoadmap = new ComboBox<>();
	private final Button createButton = new Button("Создать");
	private final Button importButton = new Button("Импортировать");

	public RoadmapsView(@Autowired RoadmapClient roadmapClient) {
		this.roadmapClient = roadmapClient;
		addClassName("roadmaps-view");

		var viewHeader = buildViewHeader();
		var roadmapLayout = buildRoadmapLayout();
		settingComponents();

		add(viewHeader, roadmapLayout);
	}

	private ViewHeader buildViewHeader() {
		var viewHeader = new ViewHeader("Карты развития");

		viewHeader.getViewName().addClassName("roadmap-view-name");
		createButton.addClassName("roadmap-create-button");
		importButton.addClassName("roadmap-import-button");

		viewHeader.addButton(changeTypeRoadmap, importButton, createButton);

		return viewHeader;
	}

	private Div buildRoadmapLayout() {
		var roadmapLayout = new Div();
		roadmapLayout.addClassName("roadmap-layout");

		getClient(RoadmapClient.class)
				.getAll(generateFilter(changeTypeRoadmap.getValue()))
				.getRoadmapResponseDtos()
				.forEach(roadmapResponseDto ->
						roadmapLayout.add(new RoadmapCard<>(roadmapResponseDto, this))
				);

		return roadmapLayout;
	}

	private void settingComponents() {
		createButton.addClickListener(event -> {
			var roadmapCardDialog = new RoadmapsCardCreateDialog();

			roadmapCardDialog.addActionButtonClickListener(action -> {
				getClient(RoadmapClient.class).create(RoadmapRequestDto.builder()
						.custom(true)
						.favorite(roadmapCardDialog.getRoadmapFavoriteComponentValue())
						.name(roadmapCardDialog.getRoadmapNameComponentValue())
						.profileId((Long) SessionHelper.getSessionAttribute(VaadinSessionAttribute.PROFILE_ID))
						.build());

				refreshView();
				roadmapCardDialog.close();
			});

			roadmapCardDialog.open();
		});

		changeTypeRoadmap.addClassName("roadmap-change-type");
		changeTypeRoadmap.setItems(RoadmapType.getAllValue());
		changeTypeRoadmap.setItemLabelGenerator(RoadmapType::getName);
		changeTypeRoadmap.setValue(RoadmapType.ALL);
		changeTypeRoadmap.addValueChangeListener(event -> refreshView());
	}

	private RoadmapFilter generateFilter(RoadmapType roadmapType) {
		var roadmapFilter = new RoadmapFilter();
		roadmapFilter.setProfileIds(List.of((Long) SessionHelper.getSessionAttribute(VaadinSessionAttribute.PROFILE_ID)));

		if (RoadmapType.ALL.equals(roadmapType)) {
			roadmapFilter.setProfileIds(List.of((Long) SessionHelper.getSessionAttribute(VaadinSessionAttribute.PROFILE_ID)));
		}

		if (RoadmapType.MY.equals(roadmapType)) {
			roadmapFilter.setCustom(true);
		}

		if (RoadmapType.DEFAULT.equals(roadmapType)) {
			roadmapFilter.setCustom(false);
		}

		if (RoadmapType.FAVORITE.equals(roadmapType)) {
			roadmapFilter.setFavorite(true);
		}

		return roadmapFilter;
	}

	@Override
	public void refreshView() {
		removeAll();

		var viewHeader = buildViewHeader();
		var roadmapLayout = buildRoadmapLayout();

		add(viewHeader, roadmapLayout);
	}

	@Override
	public <T> T getClient(Class<T> clientType) {
		if (clientType.isInstance(roadmapClient)) {
			return clientType.cast(roadmapClient);
		}

		//todo add exception
		return null;
	}
}
