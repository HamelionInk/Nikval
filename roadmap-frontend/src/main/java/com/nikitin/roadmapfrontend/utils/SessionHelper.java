package com.nikitin.roadmapfrontend.utils;

import com.nikitin.roadmapfrontend.utils.enums.VaadinSessionAttribute;
import com.vaadin.flow.component.UI;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SessionHelper {

    public Object getSessionAttribute(VaadinSessionAttribute attribute) {
        return UI.getCurrent().getSession().getAttribute(attribute.getValue());
    }
}
