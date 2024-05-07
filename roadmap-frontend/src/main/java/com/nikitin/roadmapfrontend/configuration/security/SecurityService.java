package com.nikitin.roadmapfrontend.configuration.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityService {

	public static Set<String> getAuthorities() {
		var authorities = new HashSet<String>();
		var securityContext = SecurityContextHolder.getContext();

		Optional.ofNullable(securityContext)
				.ifPresent(context ->
						authorities.addAll(context.getAuthentication().getAuthorities()
								.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toSet()))
				);

		return authorities;
	}
}
