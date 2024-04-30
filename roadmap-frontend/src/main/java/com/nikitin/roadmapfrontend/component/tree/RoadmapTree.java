package com.nikitin.roadmapfrontend.component.tree;

import com.nikitin.roadmapfrontend.client.RoadmapChapterClient;
import com.nikitin.roadmapfrontend.client.RoadmapQuestionClient;
import com.nikitin.roadmapfrontend.client.RoadmapTopicClient;
import com.nikitin.roadmapfrontend.component.CustomComponent;
import com.nikitin.roadmapfrontend.component.tree.item.AbstractNavigationItem;
import com.nikitin.roadmapfrontend.component.tree.item.FirstNavigationItem;
import com.nikitin.roadmapfrontend.component.tree.item.SecondNavigationItem;
import com.nikitin.roadmapfrontend.component.tree.item.ThirdNavigationItem;
import com.nikitin.roadmapfrontend.component.tree.menu.RoadmapTreeContextMenu;
import com.nikitin.roadmapfrontend.component.tree.observable.Observable;
import com.nikitin.roadmapfrontend.component.tree.observable.Observer;
import com.nikitin.roadmapfrontend.component.tree.utils.NavigationItemBuilder;
import com.nikitin.roadmapfrontend.component.tree.workspace.FirstNavigationWorkspace;
import com.nikitin.roadmapfrontend.component.tree.workspace.SecondNavigationWorkspace;
import com.nikitin.roadmapfrontend.component.tree.workspace.ThirdNavigationWorkspace;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapChapterFilter;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapQuestionFilter;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmapfrontend.dto.response.RoadmapResponseDto;
import com.nikitin.roadmapfrontend.utils.CookieHelper;
import com.nikitin.roadmapfrontend.utils.ScrollHelper;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class RoadmapTree extends SplitLayout implements CustomComponent, Observable {

	private static final String COOKIE_NAME_SPLITTER_POSITION = "SP";
	private final View view;
	private final RoadmapResponseDto roadmapResponseDto;
	private ContextMenu contextMenu;
	private final List<Observer> navigationObserverItems;
	private List<FirstNavigationItem> firstNavigationItems;
	private List<SecondNavigationItem> secondNavigationItems;
	private List<ThirdNavigationItem> thirdNavigationItems;

	public RoadmapTree(View view, RoadmapResponseDto roadmapResponseDto) {
		this.view = view;
		this.roadmapResponseDto = roadmapResponseDto;
		this.navigationObserverItems = new ArrayList<>();

		buildSecondarySplitLayout();
		buildNavigationItems();
		buildPrimarySplitLayout();
		buildComponent();
	}

	private void buildNavigationItems() {
		firstNavigationItems = NavigationItemBuilder
				.buildFirstNavigationItems(
						view.getClient(RoadmapChapterClient.class)
								.getAll(RoadmapChapterFilter.builder()
										.roadmapIds(List.of(roadmapResponseDto.getId()))
										.build()
								).getRoadmapChapterResponseDtos(),
						this
				);

		secondNavigationItems = NavigationItemBuilder
				.buildSecondNavigationItems(
						view.getClient(RoadmapTopicClient.class)
								.getAll(RoadmapTopicFilter.builder()
										.roadmapChapterIds(
												firstNavigationItems.stream()
														.map(FirstNavigationItem::getItemId)
														.collect(Collectors.toList()))
										.build()
								).getRoadmapTopicResponseDtos(),
						firstNavigationItems,
						this
				);

		thirdNavigationItems = NavigationItemBuilder
				.buildThirdNavigationItem(
						view.getClient(RoadmapQuestionClient.class)
								.getAll(RoadmapQuestionFilter.builder()
										.roadmapTopicIds(
												secondNavigationItems.stream()
														.map(SecondNavigationItem::getItemId)
														.collect(Collectors.toList()))
										.build()
								).getRoadmapQuestionResponseDtos(),
						secondNavigationItems,
						this
				);

		firstNavigationItems.forEach(FirstNavigationItem::buildComponent);
		secondNavigationItems.forEach(SecondNavigationItem::buildComponent);
		thirdNavigationItems.forEach(ThirdNavigationItem::buildComponent);
	}

	@Override
	public void buildComponent() {
		addClassName(StyleClassConstant.ROADMAP_TREE_SPLIT_LAYOUT);

		var abstractNavigationItems = new ArrayList<AbstractNavigationItem>();
		abstractNavigationItems.addAll(firstNavigationItems);
		abstractNavigationItems.addAll(secondNavigationItems);
		abstractNavigationItems.addAll(thirdNavigationItems);

		abstractNavigationItems.forEach(abstractNavigationItem -> {
			if (abstractNavigationItem.getIsSelected()) {
				ScrollHelper.scrollIntoComponent(abstractNavigationItem);
			}
		});

		addSplitterDragendListener(event ->
				CookieHelper.saveCookie(
						COOKIE_NAME_SPLITTER_POSITION,
						event.getSource().getSplitterPosition(),
						CookieHelper.COOKIE_AGE
				));
	}

	private void buildPrimarySplitLayout() {
		Optional.ofNullable((Div) getPrimaryComponent())
				.ifPresent(HasComponents::removeAll);

		var primaryLayout = new Div();
		primaryLayout.addClassName(StyleClassConstant.ROADMAP_TREE_LAYOUT);

		firstNavigationItems.forEach(primaryLayout::add);

		setSplitterPosition();
		addToPrimary(primaryLayout);

		contextMenu = new RoadmapTreeContextMenu(getPrimaryComponent(), this);
	}

	private void buildSecondarySplitLayout() {
		var secondaryLayout = new Div();
		secondaryLayout.addClassName(StyleClassConstant.ROADMAP_TREE_LAYOUT);
		setSplitterPosition();
		addToSecondary(secondaryLayout);
	}

	public void openWorkspace(Component component) {
		var secondaryLayout = (Div) getSecondaryComponent();
		secondaryLayout.removeAll();
		secondaryLayout.add(component);
		setSplitterPosition();
	}

	public void closeWorkspace() {
		getOpenedWorkspace(FirstNavigationWorkspace.class)
				.ifPresent(component -> {
					var firstNavigationWorkspace = (FirstNavigationWorkspace) component;

					if (firstNavigationItems.stream().noneMatch(firstNavigationItem -> Objects.equals(
							firstNavigationWorkspace.getWorkspaceId(),
							firstNavigationItem.getItemId()
					))) {
						updateSecondaryLayout();
					}
				});

		getOpenedWorkspace(SecondNavigationWorkspace.class)
				.ifPresent(component -> {
					var secondNavigationWorkspace = (SecondNavigationWorkspace) component;

					if (secondNavigationItems.stream().noneMatch(secondNavigationItem -> Objects.equals(
							secondNavigationWorkspace.getWorkspaceId(),
							secondNavigationItem.getItemId()
					))) {
						updateSecondaryLayout();
					}
				});

		getOpenedWorkspace(ThirdNavigationWorkspace.class)
				.ifPresent(component -> {
					var thirdNavigationWorkspace = (ThirdNavigationWorkspace) component;

					if (thirdNavigationItems.stream().noneMatch(thirdNavigationItem -> Objects.equals(
							thirdNavigationWorkspace.getWorkspaceId(),
							thirdNavigationItem.getItemId()
					))) {
						updateSecondaryLayout();
					}
				});
	}

	public Optional<Component> getOpenedWorkspace(Class<?> workspaceType) {
		return getSecondaryComponent()
				.getChildren()
				.filter(workspaceType::isInstance)
				.findFirst();
	}

	public void updatePrimaryLayout() {
		Optional.ofNullable(getPrimaryComponent())
				.ifPresent(component -> {
					var primaryLayout = (Div) component;

					buildNavigationItems();
					primaryLayout.removeAll();
					firstNavigationItems.forEach(primaryLayout::add);
				});
	}

	public void updateSecondaryLayout() {
		Optional.ofNullable(getSecondaryComponent())
				.ifPresent(component -> {
					var secondaryLayout = (Div) component;

					secondaryLayout.removeAll();
				});
	}

	private void setSplitterPosition() {
		var cookiePosition = CookieHelper.getCookieByName(COOKIE_NAME_SPLITTER_POSITION);

		setSplitterPosition((Objects.nonNull(cookiePosition)) ?
				Double.parseDouble(cookiePosition.getValue()) :
				20
		);
	}

	@Override
	public void addObserver(Observer observer) {
		navigationObserverItems.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		navigationObserverItems.remove(observer);
	}

	@Override
	public void notifyObserver() {
		navigationObserverItems.forEach(Observer::changeSelectedState);
	}
}
