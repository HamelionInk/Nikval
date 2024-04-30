package com.nikitin.roadmapfrontend.component.tree.dialog.chapter;

import com.nikitin.roadmapfrontend.client.RoadmapChapterClient;
import com.nikitin.roadmapfrontend.utils.constants.DialogNameConstant;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.AbstractRoadmapTreeDialog;
import com.nikitin.roadmapfrontend.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.vaadin.flow.component.textfield.TextField;

public class EditChapterDialog extends AbstractRoadmapTreeDialog {

	private final RoadmapChapterResponseDto roadmapChapterResponseDto;

	public EditChapterDialog(RoadmapTree roadmapTree, RoadmapChapterResponseDto roadmapChapterResponseDto) {
		super(roadmapTree);
		this.roadmapChapterResponseDto = roadmapChapterResponseDto;

		buildComponent();
	}

	@Override
	public void buildComponent() {
		super.buildComponent();

		setHeaderTitle(DialogNameConstant.CHAPTER_EDIT_HEADER_NAME + " - " + roadmapChapterResponseDto.getName());

		var chapterNameComponent = new TextField(DialogNameConstant.CHAPTER_NAME_COMPONENT);
		chapterNameComponent.setValue(roadmapChapterResponseDto.getName());
		chapterNameComponent.addClassName(StyleClassConstant.FULL_WIDTH);

		actionButton.setText(DialogNameConstant.ACTION_BUTTON_EDIT);
		actionButton.addClickListener(event -> {
			view.getClient(RoadmapChapterClient.class)
					.patch(
							roadmapChapterResponseDto.getId(),
							RoadmapChapterRequestDto.builder()
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
