package com.nikitin.roadmapfrontend.component;

import com.nikitin.roadmapfrontend.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ChapterCard<T extends View> extends VerticalLayout {

	private T view;
	private RoadmapChapterResponseDto roadmapChapterResponseDto;

	public ChapterCard(RoadmapChapterResponseDto roadmapChapterResponseDto, T view) {
		this.roadmapChapterResponseDto = roadmapChapterResponseDto;
		this.view = view;
		addClassName("chapter-card");

		add(new Paragraph(roadmapChapterResponseDto.getName()));
	}

}
