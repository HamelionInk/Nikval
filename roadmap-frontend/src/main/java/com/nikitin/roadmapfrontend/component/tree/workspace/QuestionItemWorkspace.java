package com.nikitin.roadmapfrontend.component.tree.workspace;

import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import lombok.Getter;

@Getter
public class QuestionItemWorkspace extends Div {

	private final Long questionId;

	public QuestionItemWorkspace(Long id, View view, Component mainComponent) {
		this.questionId = id;

		buildComponent();
		settingDoubleClickListener(view, mainComponent);
		generateData(id, view);
	}

	private void buildComponent() {

	}

	private void generateData(Long id, View view) {

	}

	private void settingDoubleClickListener(View view, Component mainComponent) {

	}
}
