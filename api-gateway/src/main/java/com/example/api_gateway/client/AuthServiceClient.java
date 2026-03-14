package com.example.api_gateway.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AuthServiceClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<Boolean> validateToken(String token){

        return webClientBuilder.build()
                .get()
                .uri("http://AUTH-SERVICE/auth/validate")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}