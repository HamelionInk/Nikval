package com.nikitin.roadmapfrontend.configuration.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends VaadinWebSecurity {

    private final KeycloakLogoutHandler keycloakLogoutHandler;
    private final SuccessAuthHandler successAuthHandler;
    private final KeycloakAuthoritiesMapper keycloakAuthoritiesMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/images/*.png")).permitAll())
                .oauth2Login(oauth2 ->
                        oauth2.userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint.userAuthoritiesMapper(keycloakAuthoritiesMapper)
                                )
                                .successHandler(successAuthHandler))
                .logout(logout ->
                        logout.logoutSuccessUrl("/")
                                .addLogoutHandler(keycloakLogoutHandler));
        super.configure(http);
    }
}
