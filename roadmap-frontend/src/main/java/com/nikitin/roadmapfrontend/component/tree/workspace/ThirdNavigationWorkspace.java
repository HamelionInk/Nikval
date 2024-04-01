package com.nikitin.roadmapfrontend.component.tree.workspace;

import com.nikitin.roadmapfrontend.component.CustomComponent;
import com.nikitin.roadmapfrontend.component.tree.item.ThirdNavigationItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ThirdNavigationWorkspace extends VerticalLayout implements CustomComponent {

	private final ThirdNavigationItem thirdNavigationItem;

	public ThirdNavigationWorkspace(ThirdNavigationItem thirdNavigationItem) {
		this.thirdNavigationItem = thirdNavigationItem;
		add(new Paragraph(thirdNavigationItem.getNavigationItemName()));
	}
	@Override
	public void buildComponent() {

	}

	public Long getWorkspaceId() {
		return thirdNavigationItem.getItemId();
	}
}
