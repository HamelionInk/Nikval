package com.nikitin.roadmapfrontend.utils;

import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.router.Route;

import java.util.Optional;

public class RouteHelper {

	public static String getURI(Class<? extends View> viewClass) {
		return Optional.ofNullable(viewClass.getAnnotation(Route.class))
				.map(route -> "/" + route.value())
				.orElseThrow(() ->
						new IllegalArgumentException("No Route annotation found for " + viewClass)
				);
	}
}
