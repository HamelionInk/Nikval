package com.nikitin.roadmaps.roadmapsbackendspring.client;

import com.nikitin.roadmaps.roadmapsbackendspring.utils.constants.ClientConstant;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
		name = ClientConstant.ROADMAP_SENDER_CLIENT,
		url = "${client.sender.url}" + "/planned-notification",
		configuration = RoadmapSenderClient.RoadmapSenderClientConfig.class
)
public interface RoadmapSenderClient {

	@PostMapping("/schedule")
	ResponseEntity<?> scheduleNotification();

	@PostMapping("/cancel-schedule")
	ResponseEntity<?> cancelScheduleNotification();

	@RequiredArgsConstructor
	class RoadmapSenderClientConfig {

		@Bean
		public RequestInterceptor requestInterceptor() {
			return requestTemplate -> {
				var authentication = SecurityContextHolder.getContext().getAuthentication();
				var oauthToken = (JwtAuthenticationToken) authentication;

				requestTemplate.header(
						"Bearer",
						oauthToken.getToken().getTokenValue()
				);
			};
		}
	}
}
