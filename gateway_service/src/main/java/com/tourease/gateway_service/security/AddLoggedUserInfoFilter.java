package com.tourease.gateway_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@SuppressWarnings("ALL")
@Configuration
public class AddLoggedUserInfoFilter implements WebFilter {

    @Autowired
    private RouteValidator validator;
    @Autowired
    private SessionHeaderResolver sessionResolver;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!validator.isSecured.test(exchange.getRequest())) {
            return chain.filter(exchange);
        }

        if (validator.isAllowed.test(exchange.getRequest())
                && exchange.getRequest().getMethod().equals(HttpMethod.GET)) {
            List<String> id = sessionResolver.resolveSessionIds(exchange);
            if (id.isEmpty()) {
                return chain.filter(exchange);
            }

            String user = sessionResolver.getIdFromSession(id.get(0));
            return chain.filter(exchange.mutate()
                    .request(exchange.getRequest().mutate()
                            .header("user", user)
                            .build())
                    .build());
        }

        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication().getName())
                .flatMap(s ->
                        chain.filter(exchange.mutate()
                                .request(exchange.getRequest().mutate()
                                        .header("user", s)
                                        .build())
                                .build())
                );
    }
}

