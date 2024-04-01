package com.nikitin.roadmapfrontend.component.tree.observable;

public interface Observable {

	void addObserver(Observer observer);
	void removeObserver(Observer observer);
	void notifyObserver();
}
