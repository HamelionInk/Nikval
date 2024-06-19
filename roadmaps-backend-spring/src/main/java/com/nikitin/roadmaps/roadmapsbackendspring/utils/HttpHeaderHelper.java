package com.nikitin.roadmaps.roadmapsbackendspring.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpHeaderHelper {

	public String generateFormDataHeaderValue(String fileName) {
		return "attachment; filename=\"" + fileName + "\"";
	}
}
