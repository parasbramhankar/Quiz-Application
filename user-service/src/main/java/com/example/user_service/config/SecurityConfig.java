package com.example.user_service.config;

import com.example.user_service.filter.HeaderAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

   private final HeaderAuthenticationFilter headerAuthenticationFilter;

   @Bean
   SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

       http.csrf(c->c.disable());

       http.authorizeHttpRequests(auth->auth
               .requestMatchers("/internal/v1/users/**").permitAll()
               .anyRequest()
               .authenticated()
       );

       http.addFilterBefore(headerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

       return http.build();

   }

}
