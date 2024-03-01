package com.nikitin.roadmapfrontend.component.tree.workspace;

import com.nikitin.roadmapfrontend.client.RoadmapQuestionClient;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapQuestionFilter;
import com.nikitin.roadmapfrontend.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import lombok.Getter;

import java.util.List;

@Getter
public class TopicItemWorkspace extends Grid<RoadmapQuestionResponseDto> {

	private final Long topicId;

	public TopicItemWorkspace(Long id, View view, Component mainComponent) {
		this.topicId = id;

		addColumn(RoadmapQuestionResponseDto::getQuestion).setHeader("Название вопроса");
		addColumn(RoadmapQuestionResponseDto::getAnswer).setHeader("Ответ");
		addColumn(RoadmapQuestionResponseDto::getIsExplored).setHeader("Освоенный");

		generateData(id, view);
		addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		setHeight("100%");
	}

	private void generateData(Long id, View view) {
		setItems(view.getClient(RoadmapQuestionClient.class)
				.getAll(RoadmapQuestionFilter.builder()
						.roadmapTopicIds(List.of(id))
						.build())
				.getRoadmapQuestionResponseDtos());
	}
}
