package com.nikitin.roadmapfrontend.component.tree.observable;

public interface SelectedObservable {

	void addObserver(SelectedItemObserver selectedItemObserver);
	void removeObserver(SelectedItemObserver selectedItemObserver);
	void notifyObserver();
}
