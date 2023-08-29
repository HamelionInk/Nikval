package com.nikitin.roadmaps.util;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
@UtilityClass
public class ViewUtils {

    public String hasStringValue(String value) {
        if(StringUtils.hasText(value)) {
            return value;
        }
        return null;
    }

    public void successNotification() {
        Notification successNotification = Notification.show("Success");
        successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        successNotification.setPosition(Notification.Position.TOP_CENTER);
    }
}
