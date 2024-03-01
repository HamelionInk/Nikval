package com.nikitin.roadmapfrontend.component.tree.dialog;

import com.nikitin.roadmapfrontend.client.RoadmapQuestionClient;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.workspace.QuestionItemWorkspace;
import com.nikitin.roadmapfrontend.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmapfrontend.utils.enums.RoadmapTreeDialogType;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Objects;

public class QuestionDialog<T extends RoadmapQuestionResponseDto, V extends View> extends AbstractRoadmapTreeDialog<T, V> {


	public QuestionDialog(RoadmapTreeDialogType type, T data, V view, Component mainComponent) {
		super(type, data, view, mainComponent);
	}

	@Override
	Component buildDialogLayout(RoadmapTreeDialogType type, T data, V view, Component mainComponent) {
		if (mainComponent instanceof RoadmapTree<?> roadmapTree) {
			switch (type) {
				case CREATE -> {
					this.addClassName("roadmap-tree-dialog-question");

					var dialogLayout = new VerticalLayout();
					var exploredCheckBox = new Checkbox("Изучен");
					var questionName = new TextField("Вопрос");
					var answerTextArea = new TextArea("Ответ");

					questionName.addClassName("full-width");
					answerTextArea.addClassName("full-width");

					dialogLayout.add(exploredCheckBox, questionName, answerTextArea);

					setHeaderTitle("Создать вопрос");

					actionButton.setText("Создать");
					actionButton.addClassName("background-3530af");
					actionButton.addClickListener(event -> {
						view
								.getClient(RoadmapQuestionClient.class)
								.create(RoadmapQuestionRequestDto.builder()
										.question(questionName.getValue())
										.answer(answerTextArea.getValue())
										.isExplored(exploredCheckBox.getValue())
										.roadmapTopicId(data.getRoadmapTopicId())
										.build());

						roadmapTree.buildPrimaryLayout();
						close();
					});

					return dialogLayout;
				}
				case EDIT -> {
					this.addClassName("roadmap-tree-dialog-question");

					var dialogLayout = new VerticalLayout();
					var exploredCheckBox = new Checkbox("Изучен");
					var questionName = new TextField("Вопрос");
					var answerTextArea = new TextArea("Ответ");

					exploredCheckBox.setValue(data.getIsExplored());
					questionName.setValue(data.getQuestion());
					answerTextArea.setValue(data.getAnswer());

					questionName.addClassName("full-width");
					answerTextArea.addClassName("full-width");

					dialogLayout.add(exploredCheckBox, questionName, answerTextArea);

					setHeaderTitle("Редактировать вопрос");

					actionButton.setText("Сохранить");
					actionButton.addClassName("background-3530af");
					actionButton.addClickListener(event -> {
						view
								.getClient(RoadmapQuestionClient.class)
								.patch(data.getId(), RoadmapQuestionRequestDto.builder()
										.question(questionName.getValue())
										.answer(answerTextArea.getValue())
										.isExplored(exploredCheckBox.getValue())
										.roadmapTopicId(data.getRoadmapTopicId())
										.build());

						roadmapTree.buildPrimaryLayout();
						close();
					});

					return dialogLayout;
				}
				case DELETE -> {
					setHeaderTitle("Удалить вопрос");

					var message = new H4(("Точно хотите удалить вопрос? - " + data.getQuestion()));
					message.addClassName("full-width");
					message.addClassName("roadmap-tree-message-dialog");

					actionButton.setText("Удалить");
					actionButton.addClassName("background-af180f");
					actionButton.addClickListener(event -> {
						view
								.getClient(RoadmapQuestionClient.class)
								.deleteById(data.getId());

						roadmapTree.buildPrimaryLayout();
						roadmapTree.getSecondaryComponentsStream()
								.forEach(component -> {
									if (component instanceof QuestionItemWorkspace questionItemWorkspace) {
										if (Objects.equals(questionItemWorkspace.getQuestionId(), data.getId())) {
											roadmapTree.buildSecondaryLayout();
										}
									}
								});
						close();
					});

					return message;
				}
			}
		}
		return null;
	}
}
