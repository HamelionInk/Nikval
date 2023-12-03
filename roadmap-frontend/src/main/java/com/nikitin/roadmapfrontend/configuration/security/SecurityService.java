package com.nikitin.roadmapfrontend.configuration.security;

import com.vaadin.flow.component.UI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityService {

    public static Set<String> getAuthorities() {
        var context = SecurityContextHolder.getContext();
        var authorities = context.getAuthentication().getAuthorities();

        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
