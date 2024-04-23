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
import com.vaadin.flow.component.textfield.TextField;
import com.wontlost.ckeditor.Constants;
import com.wontlost.ckeditor.VaadinCKEditor;
import com.wontlost.ckeditor.VaadinCKEditorBuilder;

public class ThirdNavigationWorkspace extends VerticalLayout implements CustomComponent {

	private final ThirdNavigationItem thirdNavigationItem;
	private final RoadmapQuestionResponseDto roadmapQuestionResponseDto;
	private final View view;
	private final RoadmapTree roadmapTree;
	private final Button saveButton = new Button("Сохранить");
	private final Button cancelButton = new Button("Отменить");
	private final Button editButton = new Button("Редактировать");
	private final TextField questionName = new TextField("Название вопроса");
	private final VaadinCKEditor classicEditor = new VaadinCKEditorBuilder().with(builder -> {
		builder.editorType = Constants.EditorType.CLASSIC;
		builder.theme = Constants.ThemeType.LIGHT;
		builder.readOnly = true;
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
		var workspaceLayout = new VerticalLayout();
		var workspaceButtonLayout = new HorizontalLayout();

		editButton.addClassNames(
				StyleClassConstant.DIALOG_CUSTOM_BUTTON,
				StyleClassConstant.MARGIN_LEFT_AUTO,
				StyleClassConstant.ROADMAP_TREE_QUESTION_EDIT_BUTTON
		);
		editButton.addClickListener(event -> {
			saveButton.setVisible(!saveButton.isVisible());
			cancelButton.setVisible(!cancelButton.isVisible());
			questionName.setReadOnly(questionName.isReadOnly());
			classicEditor.setReadOnly(!classicEditor.isReadOnly());
		});

		saveButton.setVisible(false);
		saveButton.addClassName(StyleClassConstant.DIALOG_BLUE_BUTTON);
		saveButton.addClickListener(event -> {
			disableEdit();

			view.getClient(RoadmapQuestionClient.class)
					.patch(roadmapQuestionResponseDto.getId(), RoadmapQuestionRequestDto.builder()
							.question(questionName.getValue())
							.answer(classicEditor.getValue())
							.build()
					);

			roadmapTree.updatePrimaryLayout();
		});

		cancelButton.setVisible(false);
		cancelButton.addClassName(StyleClassConstant.DIALOG_CUSTOM_BUTTON);
		cancelButton.addClickListener(event -> {
			disableEdit();

			questionName.setValue(roadmapQuestionResponseDto.getQuestion());
			classicEditor.setValue(roadmapQuestionResponseDto.getAnswer());
		});

		questionName.addClassNames(
				StyleClassConstant.FULL_WIDTH,
				StyleClassConstant.ROADMAP_TREE_QUESTION_NAME
		);
		questionName.setReadOnly(true);
		questionName.getElement().addEventListener("dblclick", event -> {
					questionName.setReadOnly(false);
					enableEdit();
				}
		);
		questionName.setValue(roadmapQuestionResponseDto.getQuestion());

		classicEditor.setLabel("Описание");
		classicEditor.getStyle().clear();
		classicEditor.setHeight("300px");
		classicEditor.getElement().addEventListener("dblclick", event -> {
					classicEditor.setReadOnly(false);
					enableEdit();
				}
		);
		classicEditor.setValue(roadmapQuestionResponseDto.getAnswer());

		workspaceButtonLayout.add(saveButton, cancelButton);
		workspaceLayout.add(editButton, questionName, classicEditor, workspaceButtonLayout);
		add(workspaceLayout);
	}

	private void enableEdit() {
		saveButton.setVisible(true);
		cancelButton.setVisible(true);
	}

	private void disableEdit() {
		questionName.setReadOnly(true);
		classicEditor.setReadOnly(true);
		saveButton.setVisible(false);
		cancelButton.setVisible(false);
	}

	public Long getWorkspaceId() {
		return thirdNavigationItem.getItemId();
	}
}
