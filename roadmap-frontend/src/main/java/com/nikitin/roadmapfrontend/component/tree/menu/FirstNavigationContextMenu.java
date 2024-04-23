package com.nikitin.roadmapfrontend.component.tree.menu;

import com.nikitin.roadmapfrontend.component.CustomComponent;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.chapter.CreateChapterDialog;
import com.nikitin.roadmapfrontend.component.tree.dialog.chapter.DeleteChapterDialog;
import com.nikitin.roadmapfrontend.component.tree.dialog.chapter.EditChapterDialog;
import com.nikitin.roadmapfrontend.component.tree.dialog.topic.CreateTopicDialog;
import com.nikitin.roadmapfrontend.component.tree.item.FirstNavigationItem;
import com.nikitin.roadmapfrontend.component.tree.utils.ContextMenuItemName;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.ContextMenu;

public class FirstNavigationContextMenu extends ContextMenu implements CustomComponent {

	private final RoadmapTree roadmapTree;
	private final FirstNavigationItem firstNavigationItem;

	public FirstNavigationContextMenu(Component target, RoadmapTree roadmapTree,
									  FirstNavigationItem firstNavigationItem) {
		super(target);
		this.roadmapTree = roadmapTree;
		this.firstNavigationItem = firstNavigationItem;

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

		addItem(ContextMenuItemName.CREATE_TOPIC, event -> {
			var createTopicDialog = new CreateTopicDialog(
					roadmapTree,
					firstNavigationItem.getRoadmapChapterResponseDto().getId()
			);

			createTopicDialog.open();
		});

		addItem(ContextMenuItemName.EDIT, event -> {
			var editChapterDialog = new EditChapterDialog(
					roadmapTree,
					firstNavigationItem.getRoadmapChapterResponseDto()
			);

			editChapterDialog.open();
		});

		addItem(ContextMenuItemName.DELETE, event -> {
			var deleteChapterDialog = new DeleteChapterDialog(
					roadmapTree,
					firstNavigationItem.getRoadmapChapterResponseDto()
			);

			deleteChapterDialog.open();
		});
	}
}
