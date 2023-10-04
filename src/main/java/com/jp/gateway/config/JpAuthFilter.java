package com.jp.gateway.config;



import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.SPACE;

//import static org.apache.commons.lang3.StringUtils.SPACE;


@Component
public class JpAuthFilter implements GatewayFilter {
    @Autowired
    private WebClient.Builder webClient;


    public Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return onError(exchange, HttpStatus.BAD_REQUEST);
        }
        String tokenHeader = Objects.requireNonNull(exchange
                .getRequest()
                .getHeaders()
                .get(HttpHeaders.AUTHORIZATION)).get(0);
        String[] chunks = tokenHeader.split(SPACE);
        if (chunks.length != 2 || !chunks[0].equals("Bearer")) {
            return onError(exchange, HttpStatus.BAD_REQUEST);
        }
        return webClient.build()
                .post()
                .uri("lb://JP-AUTH-SERVICE/auth/validate?token=" + chunks[1])
                .retrieve()
                .bodyToMono(TokenDto.class)
                .map(t ->
                        exchange
                ).flatMap(chain::filter);
    }


}
