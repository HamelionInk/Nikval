package com.nikitin.roadmapfrontend.component.tree.item;

import com.nikitin.roadmapfrontend.client.RoadmapQuestionClient;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.menu.ThirdNavigationContextMenu;
import com.nikitin.roadmapfrontend.component.tree.workspace.ThirdNavigationWorkspace;
import com.nikitin.roadmapfrontend.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapQuestionResponseDto;
import com.vaadin.flow.component.dnd.DropEffect;
import com.vaadin.flow.component.dnd.DropTarget;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThirdNavigationItem
		extends AbstractNavigationItem
		implements DropTarget<ThirdNavigationItem> {

	private static final String COOKIE_NAME_DROPDOWN_VISIBLE_ITEM = "TNI-";
	private static final String COOKIE_NAME_SELECTED_ITEM = "TNIS-";
	private SecondNavigationItem secondNavigationItem;
	private RoadmapQuestionResponseDto roadmapQuestionResponseDto;

	public ThirdNavigationItem(RoadmapQuestionResponseDto roadmapQuestionResponseDto, RoadmapTree roadmapTree) {
		super(roadmapQuestionResponseDto.getQuestion(), roadmapQuestionResponseDto.getId(), roadmapTree);
		this.roadmapQuestionResponseDto = roadmapQuestionResponseDto;
	}

	public void addSecondNavigationItem(SecondNavigationItem secondNavigationItem) {
		this.secondNavigationItem = secondNavigationItem;
	}

	@Override
	public void buildComponent() {
		super.buildComponent();

		itemNameComponent.setText(getNavigationItemName());

		mainLayout.add(itemNameComponent);

		contextMenu = new ThirdNavigationContextMenu(mainLayout, getRoadmapTree(), this);

		updateWorkspaceIfOpened();
		add(mainLayout);
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
		addClickListener(event -> openWorkspace());
	}

	@Override
	void settingDropTargetListener() {
		addDropListener(event ->
				event.getDragData()
						.map(dragData -> (ThirdNavigationItem) dragData)
						.ifPresent(thirdNavigationItem -> {
							var roadmapQuestionResponseDto = thirdNavigationItem.getRoadmapQuestionResponseDto();

							getRoadmapTree()
									.getView()
									.getClient(RoadmapQuestionClient.class)
									.patch(roadmapQuestionResponseDto.getId(), RoadmapQuestionRequestDto.builder()
											.position(this.roadmapQuestionResponseDto.getPosition())
											.build()
									);

							getRoadmapTree().updatePrimaryLayout();
						})
		);
	}

	@Override
	void enableDropTarget() {
		secondNavigationItem.getThirdNavigationItems()
				.forEach(thirdNavigationItem -> {
					thirdNavigationItem.setActive(true);
					thirdNavigationItem.setDropEffect(DropEffect.MOVE);
				});
	}

	@Override
	void disableDropTarget() {
		secondNavigationItem.getThirdNavigationItems()
				.forEach(thirdNavigationItem -> {
					thirdNavigationItem.setActive(false);
					thirdNavigationItem.setDropEffect(DropEffect.NONE);
				});
	}

	@Override
	public void openWorkspace() {
		getRoadmapTree().openWorkspace(new ThirdNavigationWorkspace(this));
	}

	@Override
	void updateWorkspaceIfOpened() {

	}
}
