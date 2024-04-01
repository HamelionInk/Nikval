package com.nikitin.roadmapfrontend.component.tree.item;

import com.nikitin.roadmapfrontend.component.CustomComponent;
import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.observable.Observable;
import com.nikitin.roadmapfrontend.component.tree.observable.Observer;
import com.nikitin.roadmapfrontend.icon.RoadmapIcon;
import com.nikitin.roadmapfrontend.utils.CookieHelper;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public abstract class AbstractNavigationItem
		extends Div
		implements CustomComponent, Observer, DragSource<AbstractNavigationItem> {

	private Long itemId;
	private String navigationItemName;
	private Boolean isSelected;
	private Observable observable;
	private RoadmapTree roadmapTree;
	protected ContextMenu contextMenu;
	protected Div mainLayout;
	protected Div dropDownLayout;
	protected Paragraph itemNameComponent;
	protected Button dropDownButton;

	public AbstractNavigationItem(String navigationItemName, Long itemId, RoadmapTree roadmapTree) {
		setNavigationItemName(navigationItemName);
		setItemId(itemId);
		setObservable(observable);
		setRoadmapTree(roadmapTree);
		setIsSelected(false);
	}

	@Override
	public void buildComponent() {
		roadmapTree.addObserver(this);

		mainLayout = new Div();
		mainLayout.addClassName(StyleClassConstant.ROADMAP_TREE_MAIN_LAYOUT);

		dropDownLayout = new Div();
		dropDownLayout.setVisible(false);
		dropDownLayout.addClassName(StyleClassConstant.ROADMAP_TREE_DROPDOWN_LAYOUT);

		itemNameComponent = new Paragraph();
		itemNameComponent.addClassName(StyleClassConstant.ROADMAP_TREE_ITEM_NAME_COMPONENT);

		dropDownButton = new Button(RoadmapIcon.CHEVRON_RIGHT.create());
		dropDownButton.addThemeVariants(ButtonVariant.LUMO_ICON);
		dropDownButton.addClassName(StyleClassConstant.ROADMAP_TREE_DROPDOWN_BUTTON);
		dropDownButton.addClickListener(event ->
				dropDownLayout.setVisible(openOrCloseDropDownLayout(dropDownLayout.isVisible())));

		itemNameComponent.addClickListener(event -> {
			selected();
		});

		settingClickListener();
		settingDropTargetListener();
		settingDragSource();
		buildCookie();
	}

	public void selected() {
		roadmapTree.notifyObserver();
		setIsSelected(true);
		mainLayout.addClassName(StyleClassConstant.ROADMAP_TREE_ITEM_SELECTED);
		CookieHelper.saveCookie(
				getCookieNameSelectedItem(),
				isSelected,
				CookieHelper.COOKIE_AGE
		);
	}

	public boolean openOrCloseDropDownLayout(Boolean isVisible) {
		if (isVisible) {
			dropDownButton.removeClassNames(
					StyleClassConstant.ROADMAP_TREE_ACTION_BUTTON_OPEN,
					StyleClassConstant.ROADMAP_TREE_DROPDOWN_BUTTON_OPEN
			);
			dropDownButton.addClassName(StyleClassConstant.ROADMAP_TREE_ACTION_BUTTON_CLOSE);

			return Boolean.parseBoolean(CookieHelper.saveCookie(
					getCookieNameDropdownVisibleItem(),
					false,
					CookieHelper.COOKIE_AGE
			).getValue());
		}

		dropDownButton.removeClassNames(
				StyleClassConstant.ROADMAP_TREE_ACTION_BUTTON_CLOSE,
				StyleClassConstant.ROADMAP_TREE_DROPDOWN_BUTTON_OPEN
		);
		dropDownButton.addClassName(StyleClassConstant.ROADMAP_TREE_ACTION_BUTTON_OPEN);

		return Boolean.parseBoolean(CookieHelper.saveCookie(
				getCookieNameDropdownVisibleItem(),
				true,
				CookieHelper.COOKIE_AGE
		).getValue());
	}

	protected void buildCookie() {
		Optional.ofNullable(CookieHelper.getCookieByName(
						getCookieNameDropdownVisibleItem())
				)
				.ifPresent(cookie -> {
					if (Boolean.parseBoolean(cookie.getValue())) {
						dropDownLayout.removeClassNames(
								StyleClassConstant.ROADMAP_TREE_ACTION_BUTTON_OPEN,
								StyleClassConstant.ROADMAP_TREE_ACTION_BUTTON_CLOSE
						);
						dropDownButton.addClassName(StyleClassConstant.ROADMAP_TREE_DROPDOWN_BUTTON_OPEN);
						dropDownLayout.setVisible(Boolean.parseBoolean(cookie.getValue()));
					}
				});

		Optional.ofNullable(CookieHelper.getCookieByName(
						getCookieNameSelectedItem())
				)
				.ifPresent(cookie -> {
					if (Boolean.parseBoolean(cookie.getValue())) {
						setIsSelected(Boolean.parseBoolean(cookie.getValue()));
						mainLayout.addClassName(StyleClassConstant.ROADMAP_TREE_ITEM_SELECTED);
						openWorkspace();
					} else {
						mainLayout.removeClassName(StyleClassConstant.ROADMAP_TREE_ITEM_SELECTED);
					}
				});
	}

	private void settingDragSource() {
		setDraggable(true);
		setDragData(this);

		addDragStartListener(event -> {
			itemNameComponent.addClassName(StyleClassConstant.POSSIBLE_DROP_ZONE);
			enableDropTarget();
		});

		addDragEndListener(event -> {
			itemNameComponent.removeClassName(StyleClassConstant.POSSIBLE_DROP_ZONE);
			disableDropTarget();
		});
	}

	abstract String getCookieNameDropdownVisibleItem();

	abstract String getCookieNameSelectedItem();

	abstract void settingClickListener();

	abstract void settingDropTargetListener();

	abstract void updateWorkspaceIfOpened();

	abstract void enableDropTarget();

	abstract void disableDropTarget();

	public abstract void openWorkspace();

	@Override
	public void changeSelectedState() {
		setIsSelected(false);

		CookieHelper.saveCookie(
				getCookieNameSelectedItem(),
				isSelected,
				CookieHelper.COOKIE_AGE
		);
		mainLayout.removeClassName(StyleClassConstant.ROADMAP_TREE_ITEM_SELECTED);
	}
}
