package com.nikitin.roadmapfrontend.component.tree.menu;

import com.nikitin.roadmapfrontend.component.CustomComponent;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.chapter.CreateChapterDialog;
import com.nikitin.roadmapfrontend.component.tree.dialog.question.CreateQuestionDialog;
import com.nikitin.roadmapfrontend.component.tree.dialog.topic.DeleteTopicDialog;
import com.nikitin.roadmapfrontend.component.tree.dialog.topic.EditTopicDialog;
import com.nikitin.roadmapfrontend.component.tree.item.SecondNavigationItem;
import com.nikitin.roadmapfrontend.component.tree.utils.ContextMenuItemName;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.ContextMenu;

public class SecondNavigationContextMenu extends ContextMenu implements CustomComponent {

	private final RoadmapTree roadmapTree;
	private final SecondNavigationItem secondNavigationItem;

	public SecondNavigationContextMenu(Component target, RoadmapTree roadmapTree,
									   SecondNavigationItem secondNavigationItem) {
		super(target);
		this.roadmapTree = roadmapTree;
		this.secondNavigationItem = secondNavigationItem;

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

		addItem(ContextMenuItemName.CREATE_QUESTION, event -> {
			var createQuestionDialog = new CreateQuestionDialog(
					roadmapTree,
					secondNavigationItem.getItemId()
			);

			createQuestionDialog.open();
		});

		addItem(ContextMenuItemName.EDIT, event -> {
			var editTopicDialog = new EditTopicDialog(
					roadmapTree,
					secondNavigationItem.getRoadmapTopicResponseDto()
			);

			editTopicDialog.open();
		});

		addItem(ContextMenuItemName.DELETE, event -> {
			var deleteTopicDialog = new DeleteTopicDialog(
					roadmapTree,
					secondNavigationItem.getRoadmapTopicResponseDto()
			);

			deleteTopicDialog.open();
		});
	}
}
