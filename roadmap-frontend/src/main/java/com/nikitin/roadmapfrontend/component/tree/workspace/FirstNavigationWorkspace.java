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

	private static final String NAME_HEADER = "Название темы";
	private static final String NUMBER_OF_QUESTION_HEADER = "Вопросов в теме";
	private static final String NUMBER_EXPLORED_QUESTION_HEADER = "Изученные вопросы";

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
		addColumn(RoadmapTopicResponseDto::getName).setHeader(NAME_HEADER);
		addColumn(RoadmapTopicResponseDto::getNumberOfQuestion).setHeader(NUMBER_OF_QUESTION_HEADER);
		addColumn(RoadmapTopicResponseDto::getNumberExploredQuestion).setHeader(NUMBER_EXPLORED_QUESTION_HEADER);

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

							secondNavigationItem.openWorkspace();
							secondNavigationItem.selected();

							ScrollHelper.scrollIntoComponent(secondNavigationItem);
						})
		);

		addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		addClassName("chapter-item-workspace");

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
