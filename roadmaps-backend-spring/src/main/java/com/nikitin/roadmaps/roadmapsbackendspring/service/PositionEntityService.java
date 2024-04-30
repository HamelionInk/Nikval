package com.nikitin.roadmaps.roadmapsbackendspring.service;

public interface PositionEntityService<T> {

	void positionDefinition(T entity);
	void recalculatePositions(T entity);
}
