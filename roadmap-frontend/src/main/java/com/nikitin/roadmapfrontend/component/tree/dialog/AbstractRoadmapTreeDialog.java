package com.nikitin.roadmapfrontend.component.tree.dialog;

import com.nikitin.roadmapfrontend.component.dialog.AbstractDialog;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.dto.response.RoadmapResponseDto;
import com.nikitin.roadmapfrontend.view.View;

public class AbstractRoadmapTreeDialog extends AbstractDialog {

	protected final RoadmapTree roadmapTree;
	protected final RoadmapResponseDto roadmapResponseDto;
	protected final View view;

	public AbstractRoadmapTreeDialog(RoadmapTree roadmapTree) {
		this.roadmapTree = roadmapTree;
		this.roadmapResponseDto = roadmapTree.getRoadmapResponseDto();
		this.view = roadmapTree.getView();
	}

	@Override
	public void buildComponent() {
		super.buildComponent();
	}
}
