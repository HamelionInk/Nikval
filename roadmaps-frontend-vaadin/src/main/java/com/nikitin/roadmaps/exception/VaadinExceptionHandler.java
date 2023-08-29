package com.nikitin.roadmaps.exception;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;

import java.util.Optional;

public class VaadinExceptionHandler implements ErrorHandler {

    @Override
    public void error(ErrorEvent errorEvent) {
        Optional.ofNullable(UI.getCurrent())
                .ifPresent(ui -> ui.access(() -> {
                    Notification errorNotification = Notification.show(errorEvent.getThrowable().getMessage());
                    errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    errorNotification.setPosition(Notification.Position.TOP_CENTER);
                }));
    }
}
