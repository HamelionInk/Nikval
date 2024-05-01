package com.nikitin.roadmapfrontend.component.tree.dialog.question;

import com.nikitin.roadmapfrontend.client.RoadmapQuestionClient;
import com.nikitin.roadmapfrontend.utils.constants.DialogNameConstant;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.AbstractRoadmapTreeDialog;
import com.nikitin.roadmapfrontend.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class EditQuestionDialog extends AbstractRoadmapTreeDialog {

	private final RoadmapQuestionResponseDto roadmapQuestionResponseDto;

	public EditQuestionDialog(RoadmapTree roadmapTree, RoadmapQuestionResponseDto roadmapQuestionResponseDto) {
		super(roadmapTree);
		this.roadmapQuestionResponseDto = roadmapQuestionResponseDto;

		buildComponent();
	}

	@Override
	public void buildComponent() {
		super.buildComponent();

		setHeaderTitle(DialogNameConstant.QUESTION_EDIT_HEADER_NAME);

		var componentLayout = new Div();
		var questionNameComponent = new TextField(DialogNameConstant.QUESTION_NAME_COMPONENT);
		questionNameComponent.setValue(roadmapQuestionResponseDto.getQuestion());
		questionNameComponent.addClassName(StyleClassConstant.FULL_WIDTH);

		var answerNameComponent = new TextArea(DialogNameConstant.ANSWER_NAME_COMPONENT);
		answerNameComponent.setValue(roadmapQuestionResponseDto.getAnswer());
		answerNameComponent.addClassName(StyleClassConstant.FULL_WIDTH);

		actionButton.setText(DialogNameConstant.ACTION_BUTTON_EDIT);
		actionButton.addClickListener(evnet -> {
			view.getClient(RoadmapQuestionClient.class)
					.patch(roadmapQuestionResponseDto.getId(), RoadmapQuestionRequestDto.builder()
							.question(questionNameComponent.getValue())
							.answer(answerNameComponent.getValue())
							.build()
					);

			roadmapTree.updatePrimaryLayout();

			close();
		});

		componentLayout.add(questionNameComponent, answerNameComponent);
		add(componentLayout);
	}
}
