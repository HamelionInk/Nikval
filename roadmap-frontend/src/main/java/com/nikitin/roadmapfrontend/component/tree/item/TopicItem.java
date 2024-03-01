package com.nikitin.roadmapfrontend.component.tree.item;

import com.nikitin.roadmapfrontend.client.RoadmapQuestionClient;
import com.nikitin.roadmapfrontend.client.RoadmapTopicClient;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.ChapterDialog;
import com.nikitin.roadmapfrontend.component.tree.dialog.QuestionDialog;
import com.nikitin.roadmapfrontend.component.tree.dialog.TopicDialog;
import com.nikitin.roadmapfrontend.component.tree.observable.SelectedObservable;
import com.nikitin.roadmapfrontend.component.tree.workspace.TopicItemWorkspace;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapQuestionFilter;
import com.nikitin.roadmapfrontend.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapQuestionResponseDto;
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

public class TopicItem<T extends RoadmapTopicResponseDto, V extends View>
		extends AbstractRoadmapTreeItem<T, V>
		implements DropTarget<TopicItem<T, V>> {

	public TopicItem(T data, V view, Component parentComponent,
					 Component mainComponent, SelectedObservable selectedObservable) {
		super(data, view, parentComponent, mainComponent, selectedObservable);
	}

	@Override
	protected void setMenuItems(ContextMenu contextMenu, T data, V view, Component mainComponent) {
		contextMenu.addItem("Создать раздел", event -> {
			if (mainComponent instanceof RoadmapTree<?> roadmapTree) {
				var addChapterDialog = new ChapterDialog<>(RoadmapTreeDialogType.CREATE, RoadmapChapterResponseDto.builder()
						.roadmapId(roadmapTree.getRoadmapId())
						.build(), view, mainComponent);

				addChapterDialog.open();
			}
		});

		contextMenu.addItem("Добавить вопрос", event -> {
			var addQuestionDialog = new QuestionDialog<>(RoadmapTreeDialogType.CREATE, RoadmapQuestionResponseDto.builder()
					.roadmapTopicId(data.getId())
					.build(), view, mainComponent);

			addQuestionDialog.open();
		});

		contextMenu.addItem("Редактировать", event -> {
			var editTopicDialog = new TopicDialog<>(RoadmapTreeDialogType.EDIT, data, view, mainComponent);

			editTopicDialog.open();
		});

		contextMenu.addItem("Удалить", event -> {
			var deleteTopicDialog = new TopicDialog<>(RoadmapTreeDialogType.DELETE, data, view, mainComponent);

			deleteTopicDialog.open();
		});
	}

	@Override
	protected void settingClickListener(RoadmapTopicResponseDto data, Component mainComponent, View view) {
		itemName.addClickListener(event -> {
			var splitLayout = (SplitLayout) mainComponent;
			var div = (Div) splitLayout.getSecondaryComponent();
			div.removeAll();
			div.add(new TopicItemWorkspace(data.getId(), view, mainComponent));
		});
	}

	@Override
	protected void setMainData(RoadmapTopicResponseDto data) {
		itemName.setText(data.getName());
	}

	@Override
	protected void setDropDownData(RoadmapTopicResponseDto data, View view, Component mainComponent,
								   SelectedObservable selectedObservable) {
		var items = new ArrayList<AbstractRoadmapTreeItem<RoadmapQuestionResponseDto, View>>();

		view.getClient(RoadmapQuestionClient.class).getAll(RoadmapQuestionFilter.builder()
						.roadmapTopicIds(List.of(data.getId()))
						.build()).getRoadmapQuestionResponseDtos()
				.forEach(roadmapQuestionResponseDto ->
						items.add(new QuestionItem<>(
								roadmapQuestionResponseDto,
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
	protected void settingDropTargetListener(RoadmapTopicResponseDto data, View view, Component mainComponent) {
		addDropListener(event -> {
			event.getDragData()
					.map(dragData -> (RoadmapTopicResponseDto) dragData)
					.ifPresent(dragData ->
							view
									.getClient(RoadmapTopicClient.class)
									.patch(dragData.getId(), RoadmapTopicRequestDto.builder()
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
				.map(item -> (TopicItem<T, V>) item)
				.forEach(item -> {
					item.setActive(true);
					item.setDropEffect(DropEffect.MOVE);
				});
	}

	@Override
	protected void disableDropTarget() {
		roadmapTreeItems.stream()
				.map(item -> (TopicItem<T, V>) item)
				.forEach(item -> {
					item.setActive(false);
					item.setDropEffect(DropEffect.NONE);
				});
	}
}
