package com.nikitin.roadmapfrontend.component.tree.menu;

import com.nikitin.roadmapfrontend.component.CustomComponent;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.chapter.CreateChapterDialog;
import com.nikitin.roadmapfrontend.component.tree.dialog.question.DeleteQuestionDialog;
import com.nikitin.roadmapfrontend.component.tree.dialog.question.EditQuestionDialog;
import com.nikitin.roadmapfrontend.component.tree.item.ThirdNavigationItem;
import com.nikitin.roadmapfrontend.component.tree.utils.ContextMenuItemName;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.ContextMenu;

public class ThirdNavigationContextMenu extends ContextMenu implements CustomComponent {

	private final RoadmapTree roadmapTree;
	private final ThirdNavigationItem thirdNavigationItem;

	public ThirdNavigationContextMenu(Component target, RoadmapTree roadmapTree, ThirdNavigationItem thirdNavigationItem) {
		super(target);
		this.roadmapTree = roadmapTree;
		this.thirdNavigationItem = thirdNavigationItem;

		buildComponent();
	}
	@Override
	public void buildComponent() {
		addItem(ContextMenuItemName.CREATE_CHAPTER, event -> {
			var createChapterDialog = new CreateChapterDialog(
					roadmapTree
			);

			createChapterDialog.open();
		});

		addItem(ContextMenuItemName.EDIT, event -> {
			var editQuestionDialog = new EditQuestionDialog(
					roadmapTree,
					thirdNavigationItem.getRoadmapQuestionResponseDto()
			);

			editQuestionDialog.open();
		});

		addItem(ContextMenuItemName.DELETE, event -> {
			var deleteQuestionDialog = new DeleteQuestionDialog(
					roadmapTree,
					thirdNavigationItem.getRoadmapQuestionResponseDto()
			);

			deleteQuestionDialog.open();
		});
	}
}
