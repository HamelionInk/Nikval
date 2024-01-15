package com.nikitin.roadmapfrontend.view;

import com.nikitin.roadmapfrontend.client.RoadmapChapterClient;
import com.nikitin.roadmapfrontend.client.RoadmapClient;
import com.nikitin.roadmapfrontend.client.RoadmapTopicClient;
import com.nikitin.roadmapfrontend.component.ViewHeader;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.dialog.ChapterCardDialog;
import com.nikitin.roadmapfrontend.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapResponseDto;
import com.nikitin.roadmapfrontend.utils.enums.VaadinSessionAttribute;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@PageTitle(value = "Roadmap")
@RolesAllowed(value = {"ROLE_USER", "ROLE_ADMIN"})
@Route(value = "roadmap", layout = MainView.class)
public class RoadmapView extends VerticalLayout implements HasUrlParameter<Long>, View {

	private RoadmapResponseDto roadmapResponseDto;
	private final RoadmapClient roadmapClient;
	private final RoadmapChapterClient roadmapChapterClient;
	private final RoadmapTopicClient roadmapTopicClient;

	private Button createChapterButton;
	private Button returnButton;

	public RoadmapView(@Autowired RoadmapClient roadmapClient, @Autowired RoadmapChapterClient roadmapChapterClient,
					   @Autowired RoadmapTopicClient roadmapTopicClient) {
		this.roadmapClient = roadmapClient;
		this.roadmapChapterClient = roadmapChapterClient;
		this.roadmapTopicClient = roadmapTopicClient;

		addClassName("roadmap-view");
	}

	private ViewHeader buildViewHeader() {
		var viewHeader = new ViewHeader(roadmapResponseDto.getName() + " (Разделы)");

		createChapterButton = new Button("Создать");
		createChapterButton.addClickListener(event -> {
			var createChapterDialog = new ChapterCardDialog<>(this);
			createChapterDialog.setHeaderName("Создать раздел");
			createChapterDialog.setActionButtonName("Создать");
			createChapterDialog.addActionButtonClickListener(action -> {
				getClient(RoadmapChapterClient.class).create(RoadmapChapterRequestDto.builder()
						.roadmapId(roadmapResponseDto.getId())
						.name(createChapterDialog.getChapterName())
						.build());

				refreshView();
				createChapterDialog.close();
			});
			createChapterDialog.open();
		});
		returnButton = new Button("Назад");
		returnButton.addClickListener(event ->
				UI.getCurrent().navigate((String) UI.getCurrent().getSession().getAttribute(VaadinSessionAttribute.PREVIOUS_URL.getValue())));

		viewHeader.addButton(createChapterButton, returnButton);
		return viewHeader;
	}

	private HorizontalLayout buildViewBody() {
		var viewBody = new HorizontalLayout();
		viewBody.addClassName("roadmap-view-body");

		var roadmapTree = new RoadmapTree<>(this, roadmapResponseDto.getId());

		viewBody.add(roadmapTree);
		return viewBody;
	}

	@Override
	public void refreshView() {
		removeAll();

		add(buildViewHeader(), buildViewBody());
	}

	@Override
	public <T> T getClient(Class<T> clientType) {
		if (clientType.isInstance(roadmapClient)) {
			return clientType.cast(roadmapClient);
		}

		if (clientType.isInstance(roadmapChapterClient)) {
			return clientType.cast(roadmapChapterClient);
		}

		if (clientType.isInstance(roadmapTopicClient)) {
			return clientType.cast(roadmapTopicClient);
		}

		//todo add exception
		return null;
	}

	@Override
	public void setParameter(BeforeEvent beforeEvent, Long id) {
		roadmapResponseDto = getClient(RoadmapClient.class).getById(id);

		refreshView();
	}
}
