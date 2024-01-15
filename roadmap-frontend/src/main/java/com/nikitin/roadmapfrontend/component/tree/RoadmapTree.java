package com.nikitin.roadmapfrontend.component.tree;

import com.nikitin.roadmapfrontend.client.RoadmapChapterClient;
import com.nikitin.roadmapfrontend.client.RoadmapClient;
import com.nikitin.roadmapfrontend.dto.data.RoadmapData;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapChapterFilter;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RoadmapTree<T extends View> extends Div implements DropTarget<RoadmapTree<T>> {

	private RoadmapData roadmapData;
	@Getter
	private List<ChapterTreeItem<T>> items;

	public RoadmapTree(T view, Long roadmapId) {
		roadmapData = getRoadmapData(view, roadmapId);

		addClassName("roadmap-tree");

		buildTree(view);
	}

	private RoadmapData getRoadmapData(T view, Long id) {
		return RoadmapData.builder()
				.roadmap(view.getClient(RoadmapClient.class).getById(id))
				.chapters(view.getClient(RoadmapChapterClient.class)
						.getAll(RoadmapChapterFilter.builder()
								.roadmapIds(List.of(id))
								.build())
						.getRoadmapChapterResponseDtos())
				.build();
	}

	private void buildTree(T view) {
		roadmapData.getChapters().forEach(chapter -> {
			var item = new ChapterTreeItem<>(view, this, chapter.getId());

			add(item);
		});
	}
}
