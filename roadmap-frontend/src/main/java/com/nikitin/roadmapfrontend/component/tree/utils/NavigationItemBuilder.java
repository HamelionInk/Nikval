package com.nikitin.roadmapfrontend.component.tree.utils;

import com.nikitin.roadmapfrontend.component.tree.RoadmapTree;
import com.nikitin.roadmapfrontend.component.tree.item.FirstNavigationItem;
import com.nikitin.roadmapfrontend.component.tree.item.SecondNavigationItem;
import com.nikitin.roadmapfrontend.component.tree.item.ThirdNavigationItem;
import com.nikitin.roadmapfrontend.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmapfrontend.dto.response.RoadmapTopicResponseDto;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class NavigationItemBuilder {

	public List<FirstNavigationItem> buildFirstNavigationItems(List<RoadmapChapterResponseDto> roadmapChapterResponseDtos,
															   RoadmapTree roadmapTree) {
		return roadmapChapterResponseDtos.stream()
				.map(roadmapChapterResponseDto -> new FirstNavigationItem(
						roadmapChapterResponseDto,
						roadmapTree
				))
				.collect(Collectors.toList());
	}

	public List<SecondNavigationItem> buildSecondNavigationItems(List<RoadmapTopicResponseDto> roadmapTopicResponseDtos,
																 List<FirstNavigationItem> firstNavigationItems,
																 RoadmapTree roadmapTree) {
		return roadmapTopicResponseDtos.stream()
				.map(roadmapTopicResponseDto -> {
					var secondNavigationItem = new SecondNavigationItem(
							roadmapTopicResponseDto,
							roadmapTree
					);

					firstNavigationItems.forEach(firstNavigationItem -> {
						if (firstNavigationItem.getItemId().equals(roadmapTopicResponseDto.getRoadmapChapterId())) {
							firstNavigationItem.addSecondNavigationItem(secondNavigationItem);
							secondNavigationItem.addFirstNavigationItem(firstNavigationItem);
						}
					});

					return secondNavigationItem;
				})
				.collect(Collectors.toList());
	}

	public List<ThirdNavigationItem> buildThirdNavigationItem(List<RoadmapQuestionResponseDto> roadmapQuestionResponseDtos,
															  List<SecondNavigationItem> secondNavigationItems,
															  RoadmapTree roadmapTree) {
		return roadmapQuestionResponseDtos.stream()
				.map(roadmapQuestionResponseDto -> {
					var thirdNavigationItem = new ThirdNavigationItem(
							roadmapQuestionResponseDto,
							roadmapTree
					);

					secondNavigationItems.forEach(secondNavigationItem -> {
						if (secondNavigationItem.getItemId().equals(roadmapQuestionResponseDto.getRoadmapTopicId())) {
							secondNavigationItem.addThirdNavigationItem(thirdNavigationItem);
							thirdNavigationItem.addSecondNavigationItem(secondNavigationItem);
						}
					});

					return thirdNavigationItem;
				})
				.collect(Collectors.toList());
	}
}
