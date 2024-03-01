package com.nikitin.roadmapfrontend.component.tree.workspace;

import com.nikitin.roadmapfrontend.client.RoadmapTopicClient;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmapfrontend.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class ChapterItemWorkspace extends Grid<RoadmapTopicResponseDto> {

	private final Long chapterId;

	public ChapterItemWorkspace(Long id, View view, Component mainComponent) {
		this.chapterId = id;

		buildComponent();
		settingDoubleClickListener(view, mainComponent);
		generateData(id, view);
	}

	private void buildComponent() {
		addColumn(RoadmapTopicResponseDto::getName).setHeader("Название темы");
		addColumn(RoadmapTopicResponseDto::getNumberOfQuestion).setHeader("Кол-во вопросов");
		addColumn(RoadmapTopicResponseDto::getNumberExploredQuestion).setHeader("Кол-во освоенных");

		addClassName("chapter-item-workspace");
		addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

		var contextMenu = new GridContextMenu<>(this);
		var addTopicItem = contextMenu.addItem("Создать тему");
		var editTopicItem = contextMenu.addItem("Редактировать");
		var deleteTopicItem = contextMenu.addItem("Удалить");

		contextMenu.setDynamicContentHandler(roadmapTopicResponseDto -> {
			if (Objects.isNull(roadmapTopicResponseDto)) {
				editTopicItem.setVisible(false);
				deleteTopicItem.setVisible(false);
			} else {
				editTopicItem.setVisible(true);
				deleteTopicItem.setVisible(true);
			}

			return true;
		});
	}

	private void generateData(Long id, View view) {
		setItems(view.getClient(RoadmapTopicClient.class)
				.getAll(RoadmapTopicFilter.builder()
						.roadmapChapterIds(List.of(id))
						.build())
				.getRoadmapTopicResponseDtos());

		System.out.println("Lol");
	}

	private void settingDoubleClickListener(View view, Component mainComponent) {
		addItemDoubleClickListener(event -> {
			var splitLayout = (SplitLayout) mainComponent;
			var div = (Div) splitLayout.getSecondaryComponent();
			div.removeAll();
			div.add(new TopicItemWorkspace(event.getItem().getId(), view, mainComponent));
		});
	}
}
