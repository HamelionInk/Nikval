package com.nikitin.roadmapfrontend.component.tree;

import com.nikitin.roadmapfrontend.client.RoadmapChapterClient;
import com.nikitin.roadmapfrontend.component.tree.dialog.ChapterDialog;
import com.nikitin.roadmapfrontend.component.tree.item.AbstractRoadmapTreeItem;
import com.nikitin.roadmapfrontend.component.tree.item.ChapterItem;
import com.nikitin.roadmapfrontend.component.tree.observable.SelectedObservable;
import com.nikitin.roadmapfrontend.component.tree.observable.SelectedItemObserver;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapChapterFilter;
import com.nikitin.roadmapfrontend.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmapfrontend.utils.CookieHelper;
import com.nikitin.roadmapfrontend.utils.enums.RoadmapTreeDialogType;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Getter
@Setter
public class RoadmapTree<V extends View> extends SplitLayout implements SelectedObservable {

	private final V view;
	private final Long roadmapId;
	private ContextMenu contextMenu;
	private List<SelectedItemObserver> selectedItemObserverItems = new ArrayList<>();

	public RoadmapTree(V view, Long roadmapId) {
		this.view = view;
		this.roadmapId = roadmapId;

		buildComponent();
	}

	private void buildComponent() {
		contextMenu = new ContextMenu(this);
		contextMenu.addItem("Создать раздел", event -> {
			var addChapterDialog = new ChapterDialog<>(RoadmapTreeDialogType.CREATE, RoadmapChapterResponseDto.builder()
					.roadmapId(roadmapId)
					.build(), view, this);

			addChapterDialog.open();
		});

		addClassName("roadmap-tree-split-layout");
		addSplitterDragendListener(event ->
				CookieHelper.saveCookie(
						"roadmap-tree-splitter-position",
						event.getSource().getSplitterPosition(),
						30 * 60
				));

		buildPrimaryLayout();
		buildSecondaryLayout();
	}

	public void buildPrimaryLayout() {
		Optional.ofNullable((Div) getPrimaryComponent())
				.ifPresent(HasComponents::removeAll);

		var roadmapTreePrimaryLayout = new Div();
		roadmapTreePrimaryLayout.addClassName("roadmap-tree-layout");
		var roadmapItems = new ArrayList<AbstractRoadmapTreeItem<RoadmapChapterResponseDto, View>>();

		view.getClient(RoadmapChapterClient.class).getAll(RoadmapChapterFilter.builder()
						.roadmapIds(List.of(roadmapId))
						.build())
				.getRoadmapChapterResponseDtos()
				.forEach(roadmapChapterResponseDto ->
						roadmapItems.add(new ChapterItem<>(
								roadmapChapterResponseDto,
								view,
								this,
								this,
								this)
						)
				);

		roadmapItems.forEach(item -> item.setRoadmapTreeItems(roadmapItems));

		roadmapItems.stream()
				.map(item -> (Component) item)
				.forEach(roadmapTreePrimaryLayout::add);

		saveSplitterPosition();
		addToPrimary(roadmapTreePrimaryLayout);
	}

	public void buildSecondaryLayout() {
		Optional.ofNullable((Div) getSecondaryComponent())
				.ifPresent(HasComponents::removeAll);

		var roadmapTreeSecondaryLayout = new Div();
		roadmapTreeSecondaryLayout.addClassName("roadmap-tree-layout");

		saveSplitterPosition();
		addToSecondary(roadmapTreeSecondaryLayout);
	}

	public Stream<Component> getSecondaryComponentsStream() {
		return Optional.ofNullable((Div) getSecondaryComponent())
				.map(Component::getChildren)
				.orElse(Stream.empty());
	}

	private void saveSplitterPosition() {
		var cookiePosition = CookieHelper.getCookieByName("roadmap-tree-splitter-position");

		setSplitterPosition((Objects.nonNull(cookiePosition)) ?
				Double.parseDouble(cookiePosition.getValue()) :
				20
		);
	}

	@Override
	public void addObserver(SelectedItemObserver selectedItemObserver) {
		selectedItemObserverItems.add(selectedItemObserver);
	}

	@Override
	public void removeObserver(SelectedItemObserver selectedItemObserver) {
		selectedItemObserverItems.remove(selectedItemObserver);
	}

	@Override
	public void notifyObserver() {
		selectedItemObserverItems.forEach(SelectedItemObserver::changeSelectedState);
	}
}
