package com.nikitin.roadmapfrontend.component.tree.dialog;

import com.nikitin.roadmapfrontend.client.RoadmapChapterClient;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.workspace.ChapterItemWorkspace;
import com.nikitin.roadmapfrontend.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmapfrontend.utils.enums.RoadmapTreeDialogType;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Objects;

public class ChapterDialog<T extends RoadmapChapterResponseDto, V extends View> extends AbstractRoadmapTreeDialog<T, V> {

	public ChapterDialog(RoadmapTreeDialogType type, T roadmapChapterResponseDto, V view, Component mainComponent) {
		super(type, roadmapChapterResponseDto, view, mainComponent);
	}

	@Override
	Component buildDialogLayout(RoadmapTreeDialogType type, RoadmapChapterResponseDto data, View view, Component mainComponent) {
		var roadmapTree = (RoadmapTree<View>) mainComponent;

		switch (type) {
			case CREATE -> {
				setHeaderTitle("Создать раздел");

				var chapterName = new TextField("Название раздела");
				chapterName.addClassName("full-width");

				actionButton.setText("Создать");
				actionButton.addClassName("background-3530af");
				actionButton.addClickListener(event -> {
					view
							.getClient(RoadmapChapterClient.class)
							.create(RoadmapChapterRequestDto.builder()
									.roadmapId(data.getRoadmapId())
									.name(chapterName.getValue())
									.build());

					roadmapTree.buildPrimaryLayout();
					close();
				});

				return chapterName;
			}
			case EDIT -> {
				setHeaderTitle("Редактировать раздел");

				var chapterName = new TextField("Название раздела");
				chapterName.setValue(data.getName());
				chapterName.addClassName("full-width");

				actionButton.setText("Сохранить");
				actionButton.addClassName("background-3530af");
				actionButton.addClickListener(event -> {
					view
							.getClient(RoadmapChapterClient.class)
							.patch(data.getId(), RoadmapChapterRequestDto.builder()
									.name(chapterName.getValue())
									.build());

					roadmapTree.buildPrimaryLayout();
					close();
				});

				return chapterName;
			}
			case DELETE -> {
				setHeaderTitle("Удалить раздел");

				var message = new H4("Точно хотите удалить раздел? - " + data.getName());
				message.addClassName("full-width");
				message.addClassName("roadmap-tree-message-dialog");

				actionButton.setText("Удалить");
				actionButton.addClassName("background-af180f");
				actionButton.addClickListener(event -> {
					view
							.getClient(RoadmapChapterClient.class)
							.deleteById(data.getId());


					roadmapTree.buildPrimaryLayout();
					roadmapTree.getSecondaryComponentsStream()
							.forEach(component -> {
								if (component instanceof ChapterItemWorkspace chapterItemWorkspace) {
									if (Objects.equals(chapterItemWorkspace.getChapterId(), data.getId())) {
										roadmapTree.buildSecondaryLayout();
									}
								}
							});
					close();
				});

				return message;
			}
			default -> {
				return null;
			}
		}
	}
}
