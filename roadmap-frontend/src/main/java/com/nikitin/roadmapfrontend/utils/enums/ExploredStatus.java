package com.nikitin.roadmapfrontend.utils.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ExploredStatus {

	EXPLORED("Изученный"),
	IN_PROGRESS_EXPLORED("В процессе"),
	NOT_EXPLORED("Неизученный");

	private final String value;

	ExploredStatus(String value) {
		this.value = value;
	}

	public static ExploredStatus getByValue(String value) {
		return Arrays.stream(ExploredStatus.values())
				.filter(exploredStatus -> exploredStatus.getValue().equalsIgnoreCase(value))
				.findFirst()
				.orElse(null);
	}
}
