package com.nikitin.roadmapfrontend.utils;

import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class CookieHelper {

	public Cookie saveCookie(String name, Object value, Integer age) {
		var isOpenedCookie = new Cookie(name, String.valueOf(value));
		isOpenedCookie.setMaxAge(age);
		isOpenedCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
		VaadinService.getCurrentResponse().addCookie(isOpenedCookie);

		return isOpenedCookie;
	}

	public Cookie getCookieByName(String name) {
		var cookies = VaadinService.getCurrentRequest().getCookies();

		for (Cookie cookie: cookies) {
			if (Objects.equals(cookie.getName(), name)) {
				return cookie;
			}
		}

		//todo - add exception
		return null;
	}
}
