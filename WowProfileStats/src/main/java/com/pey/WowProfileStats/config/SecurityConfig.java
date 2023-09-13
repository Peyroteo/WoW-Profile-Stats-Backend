package com.pey.WowProfileStats.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests(auth -> {auth
                        .requestMatchers("/welcome/**").permitAll()
                        .requestMatchers("/authorized/**").authenticated();
                })
                .oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults())
                .logout(logout -> logout.logoutSuccessHandler(logoutSuccessHandler()));

        return http.build();
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            final UriComponents logoutRedirect = UriComponentsBuilder.fromHttpUrl("https://battle.net")
                    .pathSegment("login", "logout")
                    .queryParam("ref", "http://localhost:8080/welcome/")
                    .build();
            response.sendRedirect(logoutRedirect.toString());
        };
    }
}
