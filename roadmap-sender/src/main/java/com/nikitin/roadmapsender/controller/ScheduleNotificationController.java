package com.nikitin.roadmapsender.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("schedule-notification")
public class ScheduleNotificationController {

	@PostMapping("/schedule")
	public ResponseEntity<?> scheduleNotification() {
		return null;
	}

	@PostMapping("/cancel-schedule")
	public ResponseEntity<?> cancelScheduleNotification() {
		return null;
	}
}
