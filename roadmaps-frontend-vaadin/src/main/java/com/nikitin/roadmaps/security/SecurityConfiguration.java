package com.nikitin.roadmaps.security;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableVaadin
@EnableWebSecurity
public class SecurityConfiguration extends VaadinWebSecurity {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/persons")))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(AntPathRequestMatcher.antMatcher("/login")).permitAll();
                    auth.requestMatchers(AntPathRequestMatcher.antMatcher("/signUp")).permitAll();
                    auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/persons")).permitAll();
                    auth.requestMatchers(AntPathRequestMatcher.antMatcher("/images/*.png")).permitAll();
                    auth.requestMatchers(AntPathRequestMatcher.antMatcher("/images/*.jpg")).permitAll();
                })
                .formLogin(login -> {
                    login.loginPage("/login").permitAll();
                    login.loginProcessingUrl("/login");
                    login.defaultSuccessUrl("/homePage", true);
                    login.failureUrl("/login?error=true");
                });
        super.configure(http);
    }
}
