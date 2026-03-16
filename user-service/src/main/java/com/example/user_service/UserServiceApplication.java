package com.example.user_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@OpenAPIDefinition(
		info = @Info(
				title = "User Service API",
				version = "v1.0",
				description = "User Service manages user profiles, user information, and user-related operations for the Quiz Platform.",
				contact = @Contact(
						name = "Quiz Platform Development Team",
						email = "support@quizplatform.com"
				)
		)
)
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
