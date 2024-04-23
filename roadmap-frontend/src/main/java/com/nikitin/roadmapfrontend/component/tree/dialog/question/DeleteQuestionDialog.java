package com.nikitin.roadmapfrontend.component.tree.dialog.question;

import com.nikitin.roadmapfrontend.client.RoadmapQuestionClient;
import com.nikitin.roadmapfrontend.utils.constants.DialogNameConstant;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.AbstractRoadmapTreeDialog;
import com.nikitin.roadmapfrontend.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.vaadin.flow.component.html.Paragraph;

public class DeleteQuestionDialog extends AbstractRoadmapTreeDialog {

	private final RoadmapQuestionResponseDto roadmapQuestionResponseDto;

	public DeleteQuestionDialog(RoadmapTree roadmapTree, RoadmapQuestionResponseDto roadmapQuestionResponseDto) {
		super(roadmapTree);
		this.roadmapQuestionResponseDto = roadmapQuestionResponseDto;

		buildComponent();
	}

	@Override
	public void buildComponent() {
		super.buildComponent();

		setHeaderTitle(DialogNameConstant.QUESTION_DELETE_HEADER_NAME + " - " + roadmapQuestionResponseDto.getQuestion());

		var deleteMessage = new Paragraph(DialogNameConstant.QUESTION_DELETE_MESSAGE);
		deleteMessage.addClassNames(
				StyleClassConstant.DIALOG_DELETE_MESSAGE,
				StyleClassConstant.FULL_WIDTH
		);

		actionButton.setText(DialogNameConstant.ACTION_BUTTON_DELETE);
		actionButton.addClickListener(event -> {
			view.getClient(RoadmapQuestionClient.class)
					.deleteById(roadmapQuestionResponseDto.getId());

			roadmapTree.updatePrimaryLayout();
			roadmapTree.closeWorkspace();

			close();
		});
		actionButton.addClassName(StyleClassConstant.DIALOG_RED_BUTTON);

		add(deleteMessage);
	}
}
