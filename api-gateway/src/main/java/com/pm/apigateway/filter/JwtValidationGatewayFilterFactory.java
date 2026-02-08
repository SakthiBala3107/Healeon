package com.pm.apigateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class JwtValidationGatewayFilterFactory extends
        AbstractGatewayFilterFactory<Object> {

    private final WebClient webClient;

    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder,
                                             @Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    //    APPLY METHOD  where  we define our custom filter logic
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String token =
                    exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//check for token
            if (token == null || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

//            once got the token we cal the auth service to check the token i slegit or not if it is then we let the
//            user to go to next filter methods
            
            return webClient.get()                       // start HTTP call
                    .uri("/validate")                     // endpoint in Auth Service
                    .header(HttpHeaders.AUTHORIZATION, token) // PASS TOKEN HERE
                    .retrieve()                           // execute request
                    .toBodilessEntity()                   // ignore response body
                    .then(chain.filter(exchange));     // continue ONLY if success;
        };
    }
}