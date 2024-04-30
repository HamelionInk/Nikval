package com.nikitin.roadmapfrontend.utils;

import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class CookieHelper {

	public static final Integer COOKIE_AGE = 30 * 60;

	public Cookie saveCookie(String name, Object value, Integer age) {
		var cookie = new Cookie(name, String.valueOf(value));
		cookie.setMaxAge(age);
		cookie.setPath(VaadinService.getCurrentRequest().getContextPath());
		VaadinService.getCurrentResponse().addCookie(cookie);
		return cookie;
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
