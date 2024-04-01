package com.nikitin.roadmapfrontend.component.tree.menu;

import com.nikitin.roadmapfrontend.component.CustomComponent;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.chapter.CreateChapterDialog;
import com.nikitin.roadmapfrontend.component.tree.utils.ContextMenuItemName;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import lombok.Getter;

@Getter
public class RoadmapTreeContextMenu extends ContextMenu implements CustomComponent {

	private final RoadmapTree roadmapTree;

	public RoadmapTreeContextMenu(Component target, RoadmapTree roadmapTree) {
		super(target);
		this.roadmapTree = roadmapTree;

		buildComponent();
	}

	@Override
	public void buildComponent() {
		addItem(ContextMenuItemName.CREATE_CHAPTER, event -> {
			var createChapterDialog = new CreateChapterDialog(roadmapTree);

			createChapterDialog.open();
		});
	}
}
