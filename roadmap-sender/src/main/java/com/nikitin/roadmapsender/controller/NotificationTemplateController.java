package com.nikitin.roadmapsender.controller;

import com.nikitin.roadmapsender.service.NotificationTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notification-template")
@RequiredArgsConstructor
public class NotificationTemplateController {

  private final NotificationTemplateService notificationTemplateService;

}
