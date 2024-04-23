package com.nikitin.roadmapfrontend.component.tree.item;

import com.nikitin.roadmapfrontend.client.RoadmapChapterClient;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.menu.FirstNavigationContextMenu;
import com.nikitin.roadmapfrontend.component.tree.workspace.FirstNavigationWorkspace;
import com.nikitin.roadmapfrontend.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapChapterResponseDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.DropEffect;
import com.vaadin.flow.component.dnd.DropTarget;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
public class FirstNavigationItem
		extends AbstractNavigationItem
		implements DropTarget<FirstNavigationItem> {

	private static final String COOKIE_NAME_DROPDOWN_VISIBLE_ITEM = "FNI-";
	private static final String COOKIE_NAME_SELECTED_ITEM = "FNIS-";
	private List<SecondNavigationItem> secondNavigationItems = new ArrayList<>();
	private RoadmapChapterResponseDto roadmapChapterResponseDto;

	public FirstNavigationItem(RoadmapChapterResponseDto roadmapChapterResponseDto, RoadmapTree roadmapTree) {
		super(roadmapChapterResponseDto.getName(), roadmapChapterResponseDto.getId(), roadmapTree);
		this.roadmapChapterResponseDto = roadmapChapterResponseDto;
	}

	public void addSecondNavigationItem(SecondNavigationItem secondNavigationItem) {
		secondNavigationItems.add(secondNavigationItem);
	}

	@Override
	public void buildComponent() {
		super.buildComponent();
		itemNameComponent.setText(getNavigationItemName());

		mainLayout.add(dropDownButton, itemNameComponent);

		dropDownLayout.add(secondNavigationItems.stream()
				.map(secondNavigationItem -> (Component) secondNavigationItem)
				.collect(Collectors.toList())
		);

		contextMenu = new FirstNavigationContextMenu(
				mainLayout,
				getRoadmapTree(),
				this
		);

		updateWorkspaceIfOpened();
		add(mainLayout, dropDownLayout);
	}

	@Override
	String getCookieNameDropdownVisibleItem() {
		return COOKIE_NAME_DROPDOWN_VISIBLE_ITEM + getItemId();
	}

	@Override
	String getCookieNameSelectedItem() {
		return COOKIE_NAME_SELECTED_ITEM + getItemId();
	}

	@Override
	public void settingClickListener() {
		itemNameComponent.addClickListener(event -> openWorkspace());
	}

	@Override
	void settingDropTargetListener() {
		addDropListener(event ->
				event.getDragData()
						.map(dragData -> (FirstNavigationItem) dragData)
						.ifPresent(firstNavigationItem -> {
							var roadmapChapterResponseDto = firstNavigationItem.getRoadmapChapterResponseDto();

							getRoadmapTree()
									.getView()
									.getClient(RoadmapChapterClient.class)
									.patch(roadmapChapterResponseDto.getId(), RoadmapChapterRequestDto.builder()
											.position(this.roadmapChapterResponseDto.getPosition())
											.build()
									);

							getRoadmapTree().updatePrimaryLayout();
						})
		);
	}

	@Override
	void enableDropTarget() {
		getRoadmapTree().getFirstNavigationItems()
				.forEach(firstNavigationItem -> {
					firstNavigationItem.setActive(true);
					firstNavigationItem.setDropEffect(DropEffect.MOVE);
				});
	}

	@Override
	void disableDropTarget() {
		getRoadmapTree().getFirstNavigationItems()
				.forEach(firstNavigationItem -> {
					firstNavigationItem.setActive(false);
					firstNavigationItem.setDropEffect(DropEffect.NONE);
				});
	}

	@Override
	public void openWorkspace() {
		getRoadmapTree().openWorkspace(new FirstNavigationWorkspace(
				secondNavigationItems,
				this
		));
	}

	@Override
	void updateWorkspaceIfOpened() {
		getRoadmapTree().getOpenedWorkspace(FirstNavigationWorkspace.class)
				.ifPresent(component -> {
					var firstNavigationWorkspace = (FirstNavigationWorkspace) component;

					if (Objects.equals(
							firstNavigationWorkspace.getWorkspaceId(),
							roadmapChapterResponseDto.getId()
					)) {
						openWorkspace();
					}
				});
	}
}
