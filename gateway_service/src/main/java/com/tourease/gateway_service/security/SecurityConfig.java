package com.tourease.gateway_service.security;

import com.google.gson.Gson;
import com.tourease.gateway_service.models.UserData;
import com.tourease.gateway_service.models.UsernamePassword;
import com.tourease.gateway_service.services.CommunicationAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.session.DefaultWebSessionManager;
import org.springframework.web.server.session.InMemoryWebSessionStore;
import org.springframework.web.server.session.WebSessionManager;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import static com.tourease.gateway_service.models.Roles.*;

@Configuration
@EnableWebFlux
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private InMemoryWebSessionStore webSessionStore;
    @Autowired
    private AuthManager authManager;
    @Autowired
    private RouteValidator routeValidator;
    @Autowired
    private CommunicationAuthentication communicationAuthentication;
    private static final String FRONTEND_LOCALHOST = "http://localhost:3000";

    @Bean
    public WebSessionManager webSessionManager() {
        DefaultWebSessionManager webSessionManager = new TokenReaderWebSessionManager();
        webSessionManager.setSessionStore(webSessionStore);
        return webSessionManager;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.FORM_LOGIN)
                .logout((logout) ->
                        logout.logoutUrl("/logout")
                                .logoutHandler(new WebSessionServerLogoutHandler())
                                .logoutSuccessHandler(getServerLogoutSuccessHandler())
                        )
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(routeValidator.openUrlMatchers).permitAll()
                        .pathMatchers(routeValidator.regularAllowedUrlMatchers).hasAuthority(REGULAR.name())
                        .pathMatchers(routeValidator.hotelAllowedUrlMatchers).hasAnyAuthority(HOTEL.name(),MANAGER.name(), RECEPTIONIST.name())
                        .pathMatchers(routeValidator.adminAllowedUrlMatchers).hasAuthority(ADMIN.name())
                        .pathMatchers(routeValidator.allowedUrlMatchers).authenticated()
                        .anyExchange().denyAll()
                        );
        return http.build();
    }

    public AuthenticationWebFilter authenticationWebFilter() {
        AuthenticationWebFilter authenticationFilter = new AuthenticationWebFilter(authManager);
        authenticationFilter.setRequiresAuthenticationMatcher(this::matches);
        authenticationFilter.setAuthenticationFailureHandler(this::onAuthenticationFailure);
        authenticationFilter.setServerAuthenticationConverter(this::convertAuthentication);
        authenticationFilter.setAuthenticationSuccessHandler(this::onAuthenticationSuccess);
        authenticationFilter.setSecurityContextRepository(new WebSessionServerSecurityContextRepository());
        return authenticationFilter;
    }

    private Mono<ServerWebExchangeMatcher.MatchResult> matches(ServerWebExchange exchange) {
        return ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/login").matches(exchange);
    }

    private Mono<Authentication> convertAuthentication(ServerWebExchange exchange) {
        return Mono.from(
                        exchange.getRequest()
                                .getBody()
                                .map(SecurityConfig::readBody))
                .map(data -> new UserData(data.getUsername(), data.getPassword())
                );
    }

    private static UsernamePassword readBody(DataBuffer d) {
        Gson gson = new Gson();
        Scanner scanner = new Scanner(d.asInputStream());
        StringBuilder json = new StringBuilder();
        while (scanner.hasNext())
            json.append(scanner.next());
        return gson.fromJson(json.toString(), UsernamePassword.class);
    }

    private Mono<Void> onAuthenticationSuccess(WebFilterExchange exchange, Authentication authentication) {
        exchange.getExchange().getResponse().setStatusCode(HttpStatus.OK);
        exchange.getExchange().getResponse().getHeaders().put(HttpHeaders.CONTENT_TYPE, List.of("application/json"));
        String permissionsListString = "{\"permissions\": " + authentication.getAuthorities().stream().map(a -> "\"" + a.toString() + "\"").toList() + "}";
        byte[] bytes = permissionsListString.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getExchange().getResponse().bufferFactory().wrap(bytes);
        return exchange.getExchange().getSession()
                .map(webSession -> {
                    sessionRegistry.registerNewSession(webSession.getId(), authentication.getPrincipal());
                    return webSession;
                })
                .flatMap(webSession->communicationAuthentication.generateToken(webSession.getId())
                        .doOnSuccess(jws -> exchange.getExchange().getResponse().getHeaders().set("Authorization", jws))
                        .onErrorResume(e -> {
                            System.out.println("Error generating token: " + e);
                            return Mono.empty();
                        }))
                .then(exchange.getExchange().getResponse().writeWith(Flux.just(buffer)));
    }

    private Mono<Void> onAuthenticationFailure(WebFilterExchange exchange, AuthenticationException exception) {
        return Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getExchange().getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().set("Authentication-error", exception.getMessage());
        });
    }

    private ServerLogoutSuccessHandler getServerLogoutSuccessHandler() {
        return (exchange, authentication) -> Mono.fromRunnable(() -> exchange.getExchange().getResponse().setStatusCode(HttpStatus.OK));
    }

    @Bean
    public CorsConfigurationSource corsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.applyPermitDefaultValues();
        corsConfig.addAllowedMethod(HttpMethod.PUT);
        corsConfig.addAllowedMethod(HttpMethod.DELETE);
        corsConfig.setAllowedOrigins(List.of(FRONTEND_LOCALHOST));
        corsConfig.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}
