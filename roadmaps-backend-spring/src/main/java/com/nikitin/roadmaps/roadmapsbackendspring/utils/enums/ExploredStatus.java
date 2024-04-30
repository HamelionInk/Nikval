package com.nikitin.roadmaps.roadmapsbackendspring.utils.enums;

import lombok.Getter;

@Getter
public enum ExploredStatus {

	EXPLORED("Изученный"),
	IN_PROGRESS_EXPLORED("В процессе"),
	NOT_EXPLORED("Неизученный");

	private final String value;

	ExploredStatus(String value) {
		this.value = value;
	}
}
