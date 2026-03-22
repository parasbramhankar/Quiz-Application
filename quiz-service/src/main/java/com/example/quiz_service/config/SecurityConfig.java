package com.example.quiz_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http){
        http.csrf(c->c.disable());

        http.authorizeHttpRequests(req->req
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/quizzes/**").hasRole("USER")
                .anyRequest().authenticated()
        );
        return http.build();
    }
}

