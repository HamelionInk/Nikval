package com.nikitin.roadmapfrontend.utils;

import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.router.Route;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class RouteHelper {

	public String getURI(Class<? extends View> viewClass) {
		return Optional.ofNullable(viewClass.getAnnotation(Route.class))
				.map(route -> "/" + route.value())
				.orElseThrow(() ->
						new IllegalArgumentException("No Route annotation found")
				);
	}
}
