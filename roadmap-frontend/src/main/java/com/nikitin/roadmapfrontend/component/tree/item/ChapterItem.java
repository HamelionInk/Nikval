package com.nikitin.roadmapfrontend.component.tree.item;

import com.nikitin.roadmapfrontend.client.RoadmapChapterClient;
import com.nikitin.roadmapfrontend.client.RoadmapTopicClient;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.ChapterDialog;
import com.nikitin.roadmapfrontend.component.tree.dialog.TopicDialog;
import com.nikitin.roadmapfrontend.component.tree.observable.SelectedObservable;
import com.nikitin.roadmapfrontend.component.tree.workspace.ChapterItemWorkspace;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmapfrontend.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmapfrontend.utils.enums.RoadmapTreeDialogType;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dnd.DropEffect;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.splitlayout.SplitLayout;

import java.util.ArrayList;
import java.util.List;

public class ChapterItem<T extends RoadmapChapterResponseDto, V extends View>
		extends AbstractRoadmapTreeItem<T, V>
		implements DropTarget<ChapterItem<T, V>> {

	public ChapterItem(T data, V view, Component parentComponent,
					   Component mainComponent, SelectedObservable selectedObservable) {
		super(data, view, parentComponent, mainComponent, selectedObservable);
	}

	@Override
	protected void setMenuItems(ContextMenu contextMenu, RoadmapChapterResponseDto data, View view, Component mainComponent) {
		contextMenu.addItem("Создать раздел", event -> {
			var addChapterDialog = new ChapterDialog<>(RoadmapTreeDialogType.CREATE, RoadmapChapterResponseDto.builder()
					.roadmapId(data.getRoadmapId())
					.build(), view, mainComponent);

			addChapterDialog.open();
		});

		contextMenu.addItem("Создать тему", event -> {
			var addTopicDialog = new TopicDialog<>(RoadmapTreeDialogType.CREATE, RoadmapTopicResponseDto.builder()
					.roadmapChapterId(data.getId())
					.build(), view, mainComponent);

			addTopicDialog.open();
		});
		contextMenu.addItem("Редактировать", event -> {
			var editChapterDialog = new ChapterDialog<>(RoadmapTreeDialogType.EDIT, data, view, mainComponent);

			editChapterDialog.open();
		});

		contextMenu.addItem("Удалить", event -> {
			var deleteChapterDialog = new ChapterDialog<>(RoadmapTreeDialogType.DELETE, data, view, mainComponent);

			deleteChapterDialog.open();
		});
	}

	@Override
	protected void settingClickListener(RoadmapChapterResponseDto data, Component mainComponent, View view) {
		itemName.addClickListener(event -> {
			var splitLayout = (SplitLayout) mainComponent;
			var div = (Div) splitLayout.getSecondaryComponent();
			div.removeAll();
			div.add(new ChapterItemWorkspace(data.getId(), view, mainComponent));
		});
	}

	@Override
	protected void setMainData(RoadmapChapterResponseDto data) {
		itemName.setText(data.getName());
	}

	@Override
	protected void setDropDownData(RoadmapChapterResponseDto data, View view, Component mainComponent,
								   SelectedObservable selectedObservable) {
		var items = new ArrayList<AbstractRoadmapTreeItem<RoadmapTopicResponseDto, View>>();

		view.getClient(RoadmapTopicClient.class).getAll(RoadmapTopicFilter.builder()
						.roadmapChapterIds(List.of(data.getId()))
						.build()).getRoadmapTopicResponseDtos()
				.forEach(roadmapTopicResponseDto ->
						items.add(new TopicItem<>(roadmapTopicResponseDto,
								view,
								this,
								mainComponent,
								selectedObservable)
						)
				);

		items.forEach(item -> {
			item.setRoadmapTreeItems(items);
			dropDownDataLayout.add(item);
		});
	}

	@Override
	protected void settingDropTargetListener(RoadmapChapterResponseDto data, View view, Component mainComponent) {
		addDropListener(event -> {
			event.getDragData()
					.map(dragData -> (RoadmapChapterResponseDto) dragData)
					.ifPresent(dragData ->
							view
									.getClient(RoadmapChapterClient.class)
									.patch(dragData.getId(), RoadmapChapterRequestDto.builder()
											.position(data.getPosition())
											.build()));

			if (mainComponent instanceof RoadmapTree<?> roadmapTree) {
				roadmapTree.buildPrimaryLayout();
			}
		});
	}

	@Override
	protected void enableDropTarget() {
		roadmapTreeItems.stream()
				.map(item -> (ChapterItem<T, V>) item)
				.forEach(item -> {
					item.setActive(true);
					item.setDropEffect(DropEffect.MOVE);
				});
	}

	@Override
	protected void disableDropTarget() {
		roadmapTreeItems.stream()
				.map(item -> (ChapterItem<T, V>) item)
				.forEach(item -> {
					item.setActive(false);
					item.setDropEffect(DropEffect.NONE);
				});
	}
}
