package com.nikitin.roadmapfrontend.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ScrollOptions;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ScrollHelper {

	public void scrollIntoComponent(Component component) {
		var options = new ScrollOptions();
		options.setBehavior(ScrollOptions.Behavior.SMOOTH);
		options.setInline(ScrollOptions.Alignment.CENTER);
		options.setBlock(ScrollOptions.Alignment.CENTER);

		component.scrollIntoView(options);
	}
}
