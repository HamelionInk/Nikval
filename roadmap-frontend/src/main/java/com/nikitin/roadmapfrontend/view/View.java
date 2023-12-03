package com.nikitin.roadmapfrontend.view;

public interface View {

    void refreshView();

    <T> T getClient(Class<T> clientType);
}
