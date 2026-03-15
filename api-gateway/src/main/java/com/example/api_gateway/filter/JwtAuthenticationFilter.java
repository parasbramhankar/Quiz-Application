package com.example.api_gateway.filter;

import com.example.api_gateway.service.JwtService;
import com.example.api_gateway.util.RouteValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final JwtService jwtService;

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            // Check if route is secured
            if (validator.isSecured.test(exchange.getRequest())) {

                // Check Authorization header
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String authHeader = exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                // Extract token
                String token = authHeader.substring(7);

                try {

                    // Validate token
                    if (!jwtService.isValid(token)) {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }

                    // Extract claims
                    Claims claims = jwtService.extractClaims(token);

                    String role = claims.get("role", String.class);
                    String userId = claims.get("id", String.class);
                    String username = claims.getSubject();

                    // 6️⃣ Add headers for microservices
                    ServerHttpRequest request = exchange.getRequest()
                            .mutate()
                            .header("X-User-Role", role)
                            .header("X-User-Id", userId)
                            .header("X-Username", username)
                            .build();

                    // 7️⃣ Forward request
                    return chain.filter(exchange.mutate().request(request).build());

                } catch (Exception e) {

                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {}
}