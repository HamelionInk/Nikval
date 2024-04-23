package com.nikitin.roadmapfrontend.component.tree.dialog.chapter;

import com.nikitin.roadmapfrontend.client.RoadmapChapterClient;
import com.nikitin.roadmapfrontend.utils.constants.DialogNameConstant;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.AbstractRoadmapTreeDialog;
import com.nikitin.roadmapfrontend.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.vaadin.flow.component.textfield.TextField;

public class CreateChapterDialog extends AbstractRoadmapTreeDialog {

	public CreateChapterDialog(RoadmapTree roadmapTree) {
		super(roadmapTree);

		buildComponent();
	}

	@Override
	public void buildComponent() {
		super.buildComponent();

		setHeaderTitle(DialogNameConstant.CHAPTER_CREATE_HEADER_NAME);

		var chapterNameComponent = new TextField(DialogNameConstant.CHAPTER_NAME_COMPONENT);
		chapterNameComponent.addClassName(StyleClassConstant.FULL_WIDTH);

		actionButton.setText(DialogNameConstant.ACTION_BUTTON_CREATE);
		actionButton.addClickListener(event -> {
			view.getClient(RoadmapChapterClient.class)
					.create(RoadmapChapterRequestDto.builder()
							.roadmapId(roadmapResponseDto.getId())
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
