package com.nikitin.roadmapfrontend.component.tree.item;

import com.nikitin.roadmapfrontend.client.RoadmapTopicClient;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.menu.SecondNavigationContextMenu;
import com.nikitin.roadmapfrontend.component.tree.workspace.SecondNavigationWorkspace;
import com.nikitin.roadmapfrontend.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapTopicResponseDto;
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
public class SecondNavigationItem
		extends AbstractNavigationItem
		implements DropTarget<SecondNavigationItem> {

	private static final String COOKIE_NAME_DROPDOWN_VISIBLE_ITEM = "SNI-";
	private static final String COOKIE_NAME_SELECTED_ITEM = "SNIS-";
	private FirstNavigationItem firstNavigationItem;
	private List<ThirdNavigationItem> thirdNavigationItems = new ArrayList<>();
	private RoadmapTopicResponseDto roadmapTopicResponseDto;

	public SecondNavigationItem(RoadmapTopicResponseDto roadmapTopicResponseDto, RoadmapTree roadmapTree) {
		super(roadmapTopicResponseDto.getName(), roadmapTopicResponseDto.getId(), roadmapTree);
		this.roadmapTopicResponseDto = roadmapTopicResponseDto;
	}

	public void addFirstNavigationItem(FirstNavigationItem firstNavigationItem) {
		this.firstNavigationItem = firstNavigationItem;
	}

	public void addThirdNavigationItem(ThirdNavigationItem thirdNavigationItem) {
		this.thirdNavigationItems.add(thirdNavigationItem);
	}

	@Override
	public void buildComponent() {
		super.buildComponent();
		itemNameComponent.setText(getNavigationItemName());

		mainLayout.add(dropDownButton, itemNameComponent);

		dropDownLayout.add(thirdNavigationItems.stream()
				.map(thirdNavigationItem -> (Component) thirdNavigationItem)
				.collect(Collectors.toList())
		);

		contextMenu = new SecondNavigationContextMenu(
				mainLayout,
				getRoadmapTree(),
				this);

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
						.map(dragData -> (SecondNavigationItem) dragData)
						.ifPresent(secondNavigationItem -> {
							var roadmapTopicResponseDto = secondNavigationItem.getRoadmapTopicResponseDto();

							getRoadmapTree()
									.getView()
									.getClient(RoadmapTopicClient.class)
									.patch(roadmapTopicResponseDto.getId(), RoadmapTopicRequestDto.builder()
											.position(this.roadmapTopicResponseDto.getPosition())
											.build()
									);

							getRoadmapTree().updatePrimaryLayout();
						})
		);
	}

	@Override
	void enableDropTarget() {
		firstNavigationItem.getSecondNavigationItems()
				.forEach(secondNavigationItem -> {
					secondNavigationItem.setActive(true);
					secondNavigationItem.setDropEffect(DropEffect.MOVE);
				});
	}

	@Override
	void disableDropTarget() {
		firstNavigationItem.getSecondNavigationItems()
				.forEach(secondNavigationItem -> {
					secondNavigationItem.setActive(false);
					secondNavigationItem.setDropEffect(DropEffect.NONE);
				});
	}

	@Override
	public void openWorkspace() {
		getRoadmapTree().openWorkspace(new SecondNavigationWorkspace(
				thirdNavigationItems,
				this
		));
	}

	@Override
	public void updateWorkspaceIfOpened() {
		getRoadmapTree().getOpenedWorkspace(SecondNavigationWorkspace.class)
				.ifPresent(component -> {
					var secondNavigationWorkspace = (SecondNavigationWorkspace) component;

					if (Objects.equals(
							secondNavigationWorkspace.getWorkspaceId(),
							roadmapTopicResponseDto.getId()
					)) {
						openWorkspace();
					}
				});
	}
}
