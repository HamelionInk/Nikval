package com.nikitin.roadmapfrontend.configuration.security;

import com.nikitin.roadmapfrontend.utils.RouteHelper;
import com.nikitin.roadmapfrontend.utils.enums.HttpSessionAttribute;
import com.nikitin.roadmapfrontend.view.HomeView;
import com.vaadin.flow.spring.security.VaadinSavedRequestAwareAuthenticationSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuccessAuthHandler extends VaadinSavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		var savedRequest = (DefaultSavedRequest) request
				.getSession(false)
				.getAttribute(HttpSessionAttribute.SPRING_SECURITY_SAVED_REQUEST.getValue());

		setAlwaysUseDefaultTargetUrl(true);
		setDefaultTargetUrl(Optional.ofNullable(savedRequest)
				.map(DefaultSavedRequest::getRequestURI)
				.filter(uri -> !uri.equals("/"))
				.orElse(RouteHelper.getURI(HomeView.class))
		);

		super.onAuthenticationSuccess(request, response, authentication);
	}
}

