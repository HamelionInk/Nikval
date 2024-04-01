package com.nikitin.roadmapfrontend.component.tree.workspace;

import com.nikitin.roadmapfrontend.component.CustomComponent;
import com.nikitin.roadmapfrontend.component.tree.item.FirstNavigationItem;
import com.nikitin.roadmapfrontend.component.tree.item.SecondNavigationItem;
import com.nikitin.roadmapfrontend.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmapfrontend.utils.ScrollHelper;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class FirstNavigationWorkspace extends Grid<RoadmapTopicResponseDto> implements CustomComponent {

	private final FirstNavigationItem firstNavigationItem;
	private final List<SecondNavigationItem> secondNavigationItems;

	public FirstNavigationWorkspace(List<SecondNavigationItem> secondNavigationItems,
									FirstNavigationItem firstNavigationItem) {
		this.secondNavigationItems = secondNavigationItems;
		this.firstNavigationItem = firstNavigationItem;

		buildComponent();
	}

	@Override
	public void buildComponent() {
		addColumn(RoadmapTopicResponseDto::getName).setHeader("Название");
		addColumn(RoadmapTopicResponseDto::getNumberOfQuestion).setHeader("Вопросы");

		addItemDoubleClickListener(event ->
				secondNavigationItems.stream()
						.filter(secondNavigationItem -> Objects.equals(
								secondNavigationItem.getItemId(), event.getItem().getId())
						)
						.findFirst()
						.ifPresent(secondNavigationItem -> {
							var firstNavigationItem = secondNavigationItem.getFirstNavigationItem();

							firstNavigationItem.getDropDownLayout()
									.setVisible(firstNavigationItem.openOrCloseDropDownLayout(
											false
									));

							secondNavigationItem.setIsSelected(true);
							secondNavigationItem.openWorkspace();
							secondNavigationItem.selected();

							ScrollHelper.scrollIntoComponent(secondNavigationItem);
						}));

		addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		addClassName("chapter-item-workspace");

		getDataProvider().addDataProviderListener(dataChangeEvent ->
				getDataProvider().refreshAll()
		);

		generateData(secondNavigationItems);
	}

	private void generateData(List<SecondNavigationItem> secondNavigationItems) {
		setItems(
				secondNavigationItems.stream()
						.map(SecondNavigationItem::getRoadmapTopicResponseDto)
						.collect(Collectors.toList())
		);
	}

	public Long getWorkspaceId() {
		return firstNavigationItem.getItemId();
	}
}
