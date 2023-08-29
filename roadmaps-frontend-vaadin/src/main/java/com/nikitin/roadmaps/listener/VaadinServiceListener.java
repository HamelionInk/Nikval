package com.nikitin.roadmaps.listener;

import com.nikitin.roadmaps.exception.VaadinExceptionHandler;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class VaadinServiceListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        serviceInitEvent.getSource().addSessionInitListener(
                initEvent -> initEvent.getSession().setErrorHandler(new VaadinExceptionHandler())
        );
    }
}
