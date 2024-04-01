package com.nikitin.roadmapfrontend.component.tree.dialog.topic;

import com.nikitin.roadmapfrontend.client.RoadmapTopicClient;
import com.nikitin.roadmapfrontend.utils.constants.DialogNameConstant;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.AbstractRoadmapTreeDialog;
import com.nikitin.roadmapfrontend.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.vaadin.flow.component.textfield.TextField;

public class EditTopicDialog extends AbstractRoadmapTreeDialog {

	private final RoadmapTopicResponseDto roadmapTopicResponseDto;

	public EditTopicDialog(RoadmapTree roadmapTree, RoadmapTopicResponseDto roadmapTopicResponseDto) {
		super(roadmapTree);
		this.roadmapTopicResponseDto = roadmapTopicResponseDto;

		buildComponent();
	}

	@Override
	public void buildComponent() {
		super.buildComponent();

		setHeaderTitle(DialogNameConstant.TOPIC_EDIT_HEADER_NAME + " - " + roadmapTopicResponseDto.getName());

		var chapterNameComponent = new TextField(DialogNameConstant.TOPIC_NAME_COMPONENT);
		chapterNameComponent.setValue(roadmapTopicResponseDto.getName());
		chapterNameComponent.addClassName(StyleClassConstant.FULL_WIDTH);

		actionButton.setText(DialogNameConstant.ACTION_BUTTON_EDIT);
		actionButton.addClickListener(event -> {
			view.getClient(RoadmapTopicClient.class)
					.patch(roadmapTopicResponseDto.getId(), RoadmapTopicRequestDto.builder()
							.name(chapterNameComponent.getValue())
							.build()
					);

			roadmapTree.updatePrimaryLayout();

			close();
		});
		actionButton.addClassName(StyleClassConstant.DIALOG_BLUE_BUTTON);

		add(chapterNameComponent);
	}
}
