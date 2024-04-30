package com.nikitin.roadmapfrontend.component.tree.workspace;

import com.nikitin.roadmapfrontend.client.RoadmapQuestionClient;
import com.nikitin.roadmapfrontend.component.CustomComponent;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.item.ThirdNavigationItem;
import com.nikitin.roadmapfrontend.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.nikitin.roadmapfrontend.utils.editor.TextEditorBuilder;
import com.nikitin.roadmapfrontend.utils.enums.ExploredStatus;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.wontlost.ckeditor.VaadinCKEditor;

public class ThirdNavigationWorkspace extends VerticalLayout implements CustomComponent {

	private final ThirdNavigationItem thirdNavigationItem;
	private final RoadmapQuestionResponseDto roadmapQuestionResponseDto;
	private final View view;
	private final RoadmapTree roadmapTree;
	private final VaadinCKEditor classicEditor;
	private final Button hideAnswer = new Button("Скрыть ответ");
	private final ComboBox<ExploredStatus> changeExplored = new ComboBox<>();
	private final TextArea questionName = new TextArea("Название вопроса");

	public ThirdNavigationWorkspace(ThirdNavigationItem thirdNavigationItem) {
		this.thirdNavigationItem = thirdNavigationItem;
		this.roadmapQuestionResponseDto = thirdNavigationItem.getRoadmapQuestionResponseDto();
		this.view = thirdNavigationItem.getRoadmapTree().getView();
		this.roadmapTree = thirdNavigationItem.getRoadmapTree();
		this.classicEditor = TextEditorBuilder.getClassicCKEditor(
				roadmapQuestionResponseDto.getAnswer()
		);

		buildComponent();
	}

	@Override
	public void buildComponent() {
		var buttonLayout = new HorizontalLayout();
		buttonLayout.addClassName(StyleClassConstant.ROADMAP_TREE_BUTTON_LAYOUT);

		changeExplored.setItems(ExploredStatus.values());
		changeExplored.setItemLabelGenerator(ExploredStatus::getValue);
		changeExplored.setValue(ExploredStatus.getByValue(roadmapQuestionResponseDto.getExploredStatus()));
		changeExplored.addValueChangeListener(event -> {
			var response = view.getClient(RoadmapQuestionClient.class)
					.patch(roadmapQuestionResponseDto.getId(), RoadmapQuestionRequestDto.builder()
							.exploredStatus(event.getValue())
							.build()
					);

			thirdNavigationItem.setRoadmapQuestionResponseDto(response);
		});

		hideAnswer.addClassName(StyleClassConstant.DIALOG_CUSTOM_BUTTON);
		hideAnswer.addClickListener(event ->
				classicEditor.setVisible(!classicEditor.isVisible())
		);

		questionName.addClassNames(
				StyleClassConstant.FULL_WIDTH,
				StyleClassConstant.ROADMAP_TREE_QUESTION_NAME
		);
		questionName.setValue(roadmapQuestionResponseDto.getQuestion());
		questionName.addValueChangeListener(event -> {
			view.getClient(RoadmapQuestionClient.class)
					.patch(roadmapQuestionResponseDto.getId(), RoadmapQuestionRequestDto.builder()
							.question(event.getValue())
							.build()
					);

			roadmapTree.updatePrimaryLayout();
		});

		classicEditor.addValueChangeListener(event -> {
			var response = view.getClient(RoadmapQuestionClient.class)
					.patch(roadmapQuestionResponseDto.getId(), RoadmapQuestionRequestDto.builder()
							.answer(event.getValue())
							.build()
					);

			thirdNavigationItem.setRoadmapQuestionResponseDto(response);
		});

		classicEditor.getElement().addEventListener(
				"focusin",
				event -> classicEditor.setReadOnlyWithToolbarAction(false)
		);

		classicEditor.getElement().addEventListener(
				"focusout",
				event -> {
					classicEditor.setReadOnlyWithToolbarAction(true);
					classicEditor.setReadOnly(false);
				}
		);

		buttonLayout.add(hideAnswer, changeExplored);
		add(buttonLayout, questionName, classicEditor);
	}

	public Long getWorkspaceId() {
		return thirdNavigationItem.getItemId();
	}
}
