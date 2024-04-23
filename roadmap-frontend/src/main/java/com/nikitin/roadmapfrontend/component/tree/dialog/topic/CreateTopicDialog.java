package com.nikitin.roadmapfrontend.component.tree.dialog.topic;

import com.nikitin.roadmapfrontend.client.RoadmapTopicClient;
import com.nikitin.roadmapfrontend.utils.constants.DialogNameConstant;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.AbstractRoadmapTreeDialog;
import com.nikitin.roadmapfrontend.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.vaadin.flow.component.textfield.TextField;

public class CreateTopicDialog extends AbstractRoadmapTreeDialog {

	private final Long chapterId;

	public CreateTopicDialog(RoadmapTree roadmapTree, Long chapterId) {
		super(roadmapTree);
		this.chapterId = chapterId;

		buildComponent();
	}

	@Override
	public void buildComponent() {
		super.buildComponent();

		setHeaderTitle(DialogNameConstant.TOPIC_CREATE_HEADER_NAME);

		var chapterNameComponent = new TextField(DialogNameConstant.TOPIC_NAME_COMPONENT);
		chapterNameComponent.addClassName(StyleClassConstant.FULL_WIDTH);

		actionButton.setText(DialogNameConstant.ACTION_BUTTON_CREATE);
		actionButton.addClickListener(event -> {
			view.getClient(RoadmapTopicClient.class)
					.create(RoadmapTopicRequestDto.builder()
							.roadmapChapterId(chapterId)
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
