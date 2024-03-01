package com.nikitin.roadmapfrontend.component.tree.item;

import com.nikitin.roadmapfrontend.component.tree.observable.SelectedObservable;
import com.nikitin.roadmapfrontend.component.tree.observable.SelectedItemObserver;
import com.nikitin.roadmapfrontend.icon.RoadmapIcon;
import com.nikitin.roadmapfrontend.utils.CookieHelper;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.EffectAllowed;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public abstract class AbstractRoadmapTreeItem<T, V extends View>
		extends Div
		implements DragSource<AbstractRoadmapTreeItem<T, V>>, SelectedItemObserver {

	private static final String COOKIE_NAME = "tree-item-is-opened";
	protected Boolean selected = false;
	protected Div mainDataLayout;
	protected Div dropDownDataLayout;
	protected Button dropDownButton;
	protected Paragraph itemName;
	protected ContextMenu contextMenu;
	protected List<AbstractRoadmapTreeItem<T, V>> roadmapTreeItems;

	public AbstractRoadmapTreeItem(T data, V view, Component parentComponent,
								   Component mainComponent, SelectedObservable selectedObservable) {
		buildComponent(selectedObservable);
		settingDropTargetListener(data, view, mainComponent);
		settingDragSource(data, parentComponent);
		settingClickListener(data, mainComponent, view);
		setMainData(data);
		setDropDownData(data, view, mainComponent, selectedObservable);
		settingCookie();
		setMenuItems(contextMenu, data, view, mainComponent);
	}

	public void setRoadmapTreeItems(List<AbstractRoadmapTreeItem<T, V>> roadmapTreeItems) {
		this.roadmapTreeItems = roadmapTreeItems;
	}

	protected void open() {
		dropDownDataLayout.setVisible(
				Boolean.parseBoolean(CookieHelper.saveCookie(getCookieName(), true, 30 * 60).getValue())
		);
		addOpenedStyle();
	}

	protected void close() {
		dropDownDataLayout.setVisible(
				Boolean.parseBoolean(CookieHelper.saveCookie(getCookieName(), false, 30 * 60).getValue())
		);
		addClosedStyle();
	}

	private String getCookieName() {
		return COOKIE_NAME + "-" + itemName.getText().replace(" ", "-");
	}

	private void buildComponent(SelectedObservable selectedObservable) {
		dropDownButton = new Button(RoadmapIcon.CHEVRON_RIGHT.create());
		dropDownButton.addClassName("roadmap-tree-drop-down-button");
		dropDownButton.addThemeVariants(ButtonVariant.LUMO_ICON);
		dropDownButton.addClickListener(event -> {
			if (dropDownDataLayout.isVisible()) {
				close();
			} else {
				open();
			}
		});

		itemName = new Paragraph();
		itemName.addClassName("roadmap-tree-item-name");

		mainDataLayout = new Div(dropDownButton, itemName);
		mainDataLayout.addClassName("roadmap-tree-main-data-layout");

		dropDownDataLayout = new Div();
		dropDownDataLayout.addClassName("roadmap-tree-drop-down-data-layout");
		contextMenu = new ContextMenu(mainDataLayout);

		selectedObservable.addObserver(this);
		itemName.addClickListener(event -> {
			selected = !getSelected();
			selectedObservable.notifyObserver();
		});

		add(mainDataLayout, dropDownDataLayout);
	}

	private void settingDragSource(T data, Component itemComponent) {
		setDraggable(true);
		setEffectAllowed(EffectAllowed.MOVE);
		setDragData(data);

		addDragStartListener(event -> {
			itemComponent.addClassName("possible-drop-zone");
			enableDropTarget();
		});

		addDragEndListener(event -> {
			itemComponent.removeClassName("possible-drop-zone");
			disableDropTarget();
		});
	}

	private void settingCookie() {
		Optional.ofNullable(CookieHelper.getCookieByName(getCookieName()))
				.ifPresentOrElse(cookie -> {
							if (Boolean.parseBoolean(cookie.getValue())) {
								open();
							} else {
								close();
							}
						},
						() -> {
							CookieHelper.saveCookie(getCookieName(), false, 30 * 60);
							close();
						});
	}

	private void addOpenedStyle() {
		dropDownButton.removeClassName("roadmap-tree-action-button-close");
		dropDownButton.addClassName("roadmap-tree-action-button-open");
	}

	private void addClosedStyle() {
		dropDownButton.removeClassName("roadmap-tree-action-button-open");
		dropDownButton.addClassName("roadmap-tree-action-button-close");
	}

	protected abstract void setMainData(T data);

	protected abstract void setDropDownData(T data, V view, Component mainComponent, SelectedObservable selectedObservable);

	protected abstract void setMenuItems(ContextMenu contextMenu, T data, V view, Component mainComponent);

	protected abstract void settingClickListener(T data, Component mainComponent, V view);

	protected abstract void settingDropTargetListener(T data, V view, Component mainComponent);

	protected abstract void enableDropTarget();

	protected abstract void disableDropTarget();

	@Override
	public void changeSelectedState() {
		if (selected) {
			mainDataLayout.addClassName("roadmap-tree-item-selected");
			selected = false;
		} else {
			mainDataLayout.removeClassName("roadmap-tree-item-selected");
		}
	}
}
