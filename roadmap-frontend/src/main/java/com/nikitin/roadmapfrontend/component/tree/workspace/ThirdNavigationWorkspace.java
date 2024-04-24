package com.nikitin.roadmapfrontend.component.tree.workspace;

import com.nikitin.roadmapfrontend.client.RoadmapQuestionClient;
import com.nikitin.roadmapfrontend.component.CustomComponent;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.item.ThirdNavigationItem;
import com.nikitin.roadmapfrontend.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.wontlost.ckeditor.Constants;
import com.wontlost.ckeditor.VaadinCKEditor;
import com.wontlost.ckeditor.VaadinCKEditorBuilder;

public class ThirdNavigationWorkspace extends VerticalLayout implements CustomComponent {

	private final ThirdNavigationItem thirdNavigationItem;
	private final RoadmapQuestionResponseDto roadmapQuestionResponseDto;
	private final View view;
	private final RoadmapTree roadmapTree;
	private final Button hideAnswer = new Button("Скрыть ответ");
	private final Button changeExplored = new Button();
	private final TextArea questionName = new TextArea("Название вопроса");
	private final VaadinCKEditor classicEditor = new VaadinCKEditorBuilder().with(builder -> {
		builder.editorType = Constants.EditorType.CLASSIC;
		builder.theme = Constants.ThemeType.LIGHT;
	}).createVaadinCKEditor();

	public ThirdNavigationWorkspace(ThirdNavigationItem thirdNavigationItem) {
		this.thirdNavigationItem = thirdNavigationItem;
		this.roadmapQuestionResponseDto = thirdNavigationItem.getRoadmapQuestionResponseDto();
		this.view = thirdNavigationItem.getRoadmapTree().getView();
		this.roadmapTree = thirdNavigationItem.getRoadmapTree();

		buildComponent();
	}

	@Override
	public void buildComponent() {
		var buttonLayout = new HorizontalLayout();
		buttonLayout.addClassName(StyleClassConstant.ROADMAP_TREE_BUTTON_LAYOUT);
		changeExplored();

		changeExplored.addClickListener(event -> {
			var response = view.getClient(RoadmapQuestionClient.class)
					.patch(roadmapQuestionResponseDto.getId(), RoadmapQuestionRequestDto.builder()
							.isExplored(!thirdNavigationItem.getRoadmapQuestionResponseDto().getIsExplored())
							.build()
					);

			thirdNavigationItem.setRoadmapQuestionResponseDto(response);
			changeExplored();
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

		classicEditor.setLabel("Ответ");
		classicEditor.getStyle().clear();
		classicEditor.setHeight("300px");
		classicEditor.setValue(roadmapQuestionResponseDto.getAnswer());
		classicEditor.addValueChangeListener(event -> {
			var response = view.getClient(RoadmapQuestionClient.class)
					.patch(roadmapQuestionResponseDto.getId(), RoadmapQuestionRequestDto.builder()
							.answer(event.getValue())
							.build()
					);

			thirdNavigationItem.setRoadmapQuestionResponseDto(response);
		});

		buttonLayout.add(hideAnswer, changeExplored);
		add(buttonLayout, questionName, classicEditor);
	}

	private void changeExplored() {
		if (thirdNavigationItem.getRoadmapQuestionResponseDto().getIsExplored()) {
			changeExplored.removeClassName(StyleClassConstant.ROADMAP_TREE_BUTTON_CHANGE_NO_EXPLORED);
			changeExplored.addClassName(StyleClassConstant.ROADMAP_TREE_BUTTON_CHANGE_EXPLORED);
			changeExplored.setText("Изучено");
		} else {
			changeExplored.removeClassName(StyleClassConstant.ROADMAP_TREE_BUTTON_CHANGE_EXPLORED);
			changeExplored.addClassName(StyleClassConstant.ROADMAP_TREE_BUTTON_CHANGE_NO_EXPLORED);
			changeExplored.setText("Не изучен");
		}
	}

	public Long getWorkspaceId() {
		return thirdNavigationItem.getItemId();
	}
}
