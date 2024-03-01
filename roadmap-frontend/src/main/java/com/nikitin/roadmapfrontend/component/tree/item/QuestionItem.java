package com.nikitin.roadmapfrontend.component.tree.item;

import com.nikitin.roadmapfrontend.client.RoadmapQuestionClient;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.dialog.ChapterDialog;
import com.nikitin.roadmapfrontend.component.tree.dialog.QuestionDialog;
import com.nikitin.roadmapfrontend.component.tree.observable.SelectedObservable;
import com.nikitin.roadmapfrontend.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmapfrontend.utils.enums.RoadmapTreeDialogType;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dnd.DropEffect;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.splitlayout.SplitLayout;

public class QuestionItem<T extends RoadmapQuestionResponseDto, V extends View>
		extends AbstractRoadmapTreeItem<T, V>
		implements DropTarget<QuestionItem<T, V>> {

	public QuestionItem(T data, V view, Component parentComponent,
						Component mainComponent, SelectedObservable selectedObservable) {
		super(data, view, parentComponent, mainComponent, selectedObservable);
	}

	@Override
	protected void setMainData(T data) {
		itemName.setText(data.getQuestion());
		dropDownButton.setVisible(false);
	}

	@Override
	protected void setDropDownData(T data, V view, Component mainComponent, SelectedObservable selectedObservable) {

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

		contextMenu.addItem("Редактировать", event -> {
			var editQuestionDialog = new QuestionDialog<>(RoadmapTreeDialogType.EDIT, data, view, mainComponent);

			editQuestionDialog.open();
		});

		contextMenu.addItem("Удалить", event -> {
			var deleteQuestionDialog = new QuestionDialog<>(RoadmapTreeDialogType.DELETE, data, view, mainComponent);

			deleteQuestionDialog.open();
		});
	}

	@Override
	protected void settingClickListener(RoadmapQuestionResponseDto data, Component mainComponent, View view) {
		itemName.addClickListener(event -> {
			var splitLayout = (SplitLayout) mainComponent;
			var div = (Div) splitLayout.getSecondaryComponent();
			div.removeAll();
			div.add(new Paragraph("Cliked - " + data.getQuestion()));
		});
	}

	@Override
	protected void settingDropTargetListener(T data, V view, Component mainComponent) {
		addDropListener(event -> {
			event.getDragData()
					.map(dragData -> (RoadmapQuestionResponseDto) dragData)
					.ifPresent(dragData ->
							view
									.getClient(RoadmapQuestionClient.class)
									.patch(dragData.getId(), RoadmapQuestionRequestDto.builder()
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
				.map(item -> (QuestionItem<T, V>) item)
				.forEach(item -> {
					item.setActive(true);
					item.setDropEffect(DropEffect.MOVE);
				});
	}

	@Override
	protected void disableDropTarget() {
		roadmapTreeItems.stream()
				.map(item -> (QuestionItem<T, V>) item)
				.forEach(item -> {
					item.setActive(false);
					item.setDropEffect(DropEffect.NONE);
				});
	}
}
