package com.nikitin.roadmaps.roadmapsbackendspring.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {
	public static String replaceAll(String value, String oldValue, String newValue) {
		final int strLength = value.length();
		final int oldStrLength = oldValue.length();

		StringBuilder builder = new StringBuilder(value);

		for (int count = 0; count < strLength; count++) {
			int index = builder.indexOf(oldValue, count);

			if (index == -1) {
				if (count == 0) {
					return value;
				}
				return builder.toString();
			}

			builder.replace(index, index + oldStrLength, newValue);
		}

		return builder.toString();
	}
}
