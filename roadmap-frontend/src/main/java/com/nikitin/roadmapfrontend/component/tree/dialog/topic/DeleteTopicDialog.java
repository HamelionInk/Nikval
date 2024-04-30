package com.nikitin.roadmapfrontend.component.tree.dialog.topic;

import com.nikitin.roadmapfrontend.client.RoadmapTopicClient;
import com.nikitin.roadmapfrontend.utils.constants.DialogNameConstant;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.AbstractRoadmapTreeDialog;
import com.nikitin.roadmapfrontend.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.vaadin.flow.component.html.H4;

public class DeleteTopicDialog extends AbstractRoadmapTreeDialog {

	private final RoadmapTopicResponseDto roadmapTopicResponseDto;

	public DeleteTopicDialog(RoadmapTree roadmapTree, RoadmapTopicResponseDto roadmapTopicResponseDto) {
		super(roadmapTree);
		this.roadmapTopicResponseDto = roadmapTopicResponseDto;

		buildComponent();
	}

	@Override
	public void buildComponent() {
		super.buildComponent();

		setHeaderTitle(DialogNameConstant.TOPIC_DELETE_HEADER_NAME + " - " + roadmapTopicResponseDto.getName());

		var deleteMessage = new H4(DialogNameConstant.TOPIC_DELETE_MESSAGE);
		deleteMessage.addClassNames(
				StyleClassConstant.DIALOG_DELETE_MESSAGE,
				StyleClassConstant.FULL_WIDTH
		);

		actionButton.setText(DialogNameConstant.ACTION_BUTTON_DELETE);
		actionButton.addClickListener(event -> {
			view.getClient(RoadmapTopicClient.class)
					.deleteById(roadmapTopicResponseDto.getId());

			roadmapTree.updatePrimaryLayout();
			roadmapTree.closeWorkspace();

			close();
		});
		actionButton.addClassName(StyleClassConstant.DIALOG_RED_BUTTON);

		add(deleteMessage);
	}
}
