package com.nikitin.roadmapfrontend.component.tree.workspace;

import com.nikitin.roadmapfrontend.component.CustomComponent;
import com.nikitin.roadmapfrontend.component.tree.item.SecondNavigationItem;
import com.nikitin.roadmapfrontend.component.tree.item.ThirdNavigationItem;
import com.nikitin.roadmapfrontend.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmapfrontend.utils.ScrollHelper;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SecondNavigationWorkspace extends Grid<RoadmapQuestionResponseDto> implements CustomComponent {

	private final SecondNavigationItem secondNavigationItem;
	private final List<ThirdNavigationItem> thirdNavigationItems;

	public SecondNavigationWorkspace(List<ThirdNavigationItem> thirdNavigationItems,
									 SecondNavigationItem secondNavigationItem) {
		this.thirdNavigationItems = thirdNavigationItems;
		this.secondNavigationItem = secondNavigationItem;

		buildComponent();
	}

	@Override
	public void buildComponent() {
		addColumn(RoadmapQuestionResponseDto::getQuestion).setHeader("Вопрос");

		addItemDoubleClickListener(event -> {
			thirdNavigationItems.stream()
					.filter(thirdNavigationItem -> Objects.equals(
							thirdNavigationItem.getItemId(), event.getItem().getId())
					)
					.findFirst()
					.ifPresent(thirdNavigationItem -> {
						var secondNavigationItem = thirdNavigationItem.getSecondNavigationItem();
						var firstNavigationItem = secondNavigationItem.getFirstNavigationItem();

						firstNavigationItem.getDropDownLayout()
								.setVisible(firstNavigationItem.openOrCloseDropDownLayout(
										false
								));

						secondNavigationItem.getDropDownLayout()
								.setVisible(secondNavigationItem.openOrCloseDropDownLayout(
										false
								));

						thirdNavigationItem.setIsSelected(true);;
						thirdNavigationItem.openWorkspace();
						thirdNavigationItem.selected();

						ScrollHelper.scrollIntoComponent(thirdNavigationItem);
					});
		});

		addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		addClassName("chapter-item-workspace");

		generateData(thirdNavigationItems);
	}

	private void generateData(List<ThirdNavigationItem> thirdNavigationItems) {
		setItems(
				thirdNavigationItems.stream()
						.map(ThirdNavigationItem::getRoadmapQuestionResponseDto)
						.collect(Collectors.toList())
		);
	}

	public Long getWorkspaceId() {
		return secondNavigationItem.getItemId();
	}
}
