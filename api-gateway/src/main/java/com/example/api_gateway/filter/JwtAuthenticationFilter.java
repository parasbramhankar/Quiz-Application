package com.example.api_gateway.filter;

import com.example.api_gateway.client.AuthServiceClient;
import com.example.api_gateway.util.RouteValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final AuthServiceClient authServiceClient;

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            if (validator.isSecured.test(exchange.getRequest())) {

                if (!exchange.getRequest().getHeaders().containsKey("Authorization")) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String token = exchange.getRequest()
                        .getHeaders()
                        .getFirst("Authorization");

                return authServiceClient.validateToken(token)
                        .flatMap(valid -> {

                            if (!valid) {
                                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                return exchange.getResponse().setComplete();
                            }

                            return chain.filter(exchange);
                        });
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {}
}