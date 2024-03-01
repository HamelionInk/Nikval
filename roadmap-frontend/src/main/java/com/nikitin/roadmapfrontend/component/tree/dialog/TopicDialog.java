package com.nikitin.roadmapfrontend.component.tree.dialog;

import com.nikitin.roadmapfrontend.client.RoadmapTopicClient;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.workspace.TopicItemWorkspace;
import com.nikitin.roadmapfrontend.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmapfrontend.utils.enums.RoadmapTreeDialogType;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Objects;

public class TopicDialog<T extends RoadmapTopicResponseDto, V extends View> extends AbstractRoadmapTreeDialog<T, V> {

	public TopicDialog(RoadmapTreeDialogType type, T roadmapTopicResponseDto, V view, Component mainComponent) {
		super(type, roadmapTopicResponseDto, view, mainComponent);
	}

	@Override
	Component buildDialogLayout(RoadmapTreeDialogType type, RoadmapTopicResponseDto data, View view, Component mainComponent) {
		if (mainComponent instanceof RoadmapTree<?> roadmapTree) {
			switch (type) {
				case CREATE -> {
					setHeaderTitle("Создать тему");

					var topicName = new TextField("Название темы");
					topicName.addClassName("full-width");

					actionButton.setText("Создать");
					actionButton.addClassName("background-3530af");
					actionButton.addClickListener(event -> {
						view
								.getClient(RoadmapTopicClient.class)
								.create(RoadmapTopicRequestDto.builder()
										.roadmapChapterId(data.getRoadmapChapterId())
										.name(topicName.getValue())
										.build());

						view.refreshView();
						close();
					});

					return topicName;
				}
				case EDIT -> {
					setHeaderTitle("Редактировать тему");

					var topicName = new TextField("Название темы");
					topicName.addClassName("full-width");
					topicName.setValue(data.getName());

					actionButton.setText("Сохранить");
					actionButton.addClassName("background-3530af");
					actionButton.addClickListener(event -> {
						view
								.getClient(RoadmapTopicClient.class)
								.patch(data.getId(), RoadmapTopicRequestDto.builder()
										.roadmapChapterId(data.getRoadmapChapterId())
										.name(topicName.getValue())
										.build());

						roadmapTree.buildPrimaryLayout();
						close();
					});

					return topicName;
				}
				case DELETE -> {
					setHeaderTitle("Удалить тему");

					var message = new H4(("Точно хотите удалить тему? - " + data.getName()));
					message.addClassName("full-width");
					message.addClassName("roadmap-tree-message-dialog");

					actionButton.setText("Удалить");
					actionButton.addClassName("background-af180f");
					actionButton.addClickListener(event -> {
						view
								.getClient(RoadmapTopicClient.class)
								.deleteById(data.getId());

						roadmapTree.buildPrimaryLayout();
						roadmapTree.getSecondaryComponentsStream()
								.forEach(component -> {
									if (component instanceof TopicItemWorkspace topicItemWorkspace) {
										if (Objects.equals(topicItemWorkspace.getTopicId(), data.getId())) {
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
		return null; //todo - add exception
	}
}
