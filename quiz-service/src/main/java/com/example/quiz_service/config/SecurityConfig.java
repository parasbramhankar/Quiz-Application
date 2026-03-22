package com.example.quiz_service.config;

import com.example.quiz_service.filter.HeaderAuthFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final HeaderAuthFilter headerAuthFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http){
        http.csrf(c->c.disable());

        http.authorizeHttpRequests(req->req
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/quizzes/**").hasRole("USER")
                .anyRequest().authenticated()
        );

        http.addFilterBefore(headerAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

