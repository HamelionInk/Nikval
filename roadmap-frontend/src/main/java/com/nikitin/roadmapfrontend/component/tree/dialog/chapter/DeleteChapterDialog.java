package com.nikitin.roadmapfrontend.component.tree.dialog.chapter;

import com.nikitin.roadmapfrontend.client.RoadmapChapterClient;
import com.nikitin.roadmapfrontend.utils.constants.DialogNameConstant;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.AbstractRoadmapTreeDialog;
import com.nikitin.roadmapfrontend.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.vaadin.flow.component.html.H4;

public class DeleteChapterDialog extends AbstractRoadmapTreeDialog {

	private final RoadmapChapterResponseDto roadmapChapterResponseDto;

	public DeleteChapterDialog(RoadmapTree roadmapTree, RoadmapChapterResponseDto roadmapChapterResponseDto) {
		super(roadmapTree);
		this.roadmapChapterResponseDto = roadmapChapterResponseDto;

		buildComponent();
	}

	@Override
	public void buildComponent() {
		super.buildComponent();

		setHeaderTitle(DialogNameConstant.CHAPTER_DELETE_HEADER_NAME + " - " + roadmapChapterResponseDto.getName());

		var deleteMessage = new H4(DialogNameConstant.CHAPTER_DELETE_MESSAGE);
		deleteMessage.addClassNames(
				StyleClassConstant.DIALOG_DELETE_MESSAGE,
				StyleClassConstant.FULL_WIDTH
		);

		actionButton.setText(DialogNameConstant.ACTION_BUTTON_DELETE);
		actionButton.addClickListener(event -> {
			view.getClient(RoadmapChapterClient.class)
					.deleteById(roadmapChapterResponseDto.getId());

			roadmapTree.updatePrimaryLayout();

			close();
		});
		actionButton.addClassName(StyleClassConstant.DIALOG_RED_BUTTON);

		add(deleteMessage);
	}
}
