package com.nikitin.roadmapfrontend.component.tree.dialog.question;

import com.nikitin.roadmapfrontend.client.RoadmapQuestionClient;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.AbstractRoadmapTreeDialog;
import com.nikitin.roadmapfrontend.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmapfrontend.utils.constants.DialogNameConstant;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.nikitin.roadmapfrontend.utils.enums.ExploredStatus;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextArea;

public class CreateQuestionDialog extends AbstractRoadmapTreeDialog {

	private final Long topicId;

	public CreateQuestionDialog(RoadmapTree roadmapTree, Long topicId) {
		super(roadmapTree);
		this.topicId = topicId;

		buildComponent();
	}

	@Override
	public void buildComponent() {
		super.buildComponent();

		setHeaderTitle(DialogNameConstant.QUESTION_CREATE_HEADER_NAME);

		var componentLayout = new Div();
		var questionNameComponent = new TextArea(DialogNameConstant.QUESTION_NAME_COMPONENT);
		questionNameComponent.addClassName(StyleClassConstant.FULL_WIDTH);

		actionButton.setText(DialogNameConstant.ACTION_BUTTON_CREATE);
		actionButton.addClickListener(event -> {
			view.getClient(RoadmapQuestionClient.class)
					.create(RoadmapQuestionRequestDto.builder()
							.question(questionNameComponent.getValue())
							.roadmapTopicId(topicId)
							.exploredStatus(ExploredStatus.NOT_EXPLORED)
							.build()
					);

			roadmapTree.updatePrimaryLayout();

			close();
		});
		actionButton.addClassName(StyleClassConstant.DIALOG_BLUE_BUTTON);

		componentLayout.add(questionNameComponent);
		add(componentLayout);
	}
}
