package com.DressKlub.api_gateway.config;

import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        logger.info("Incoming request path: {}", path);

        if (isPublicRoute(path)) {
            logger.info("Public route detected. Skipping authentication.");
            return chain.filter(exchange);
        }

        List<String> cookies = request.getHeaders().get("Cookie");

        if (cookies == null || cookies.isEmpty()) {
            logger.warn("No cookies found in the request.");
            return unauthorizedResponse(exchange);
        }

        String jwtToken = jwtUtil.extractTokenFromCookie(cookies);
        if (jwtToken == null) {
            logger.warn("JWT token not found in cookies.");
            return unauthorizedResponse(exchange);
        }

        if (!jwtUtil.validateToken(jwtToken)) {
            logger.warn("Invalid JWT token.");
            return unauthorizedResponse(exchange);
        }

        Claims claims = jwtUtil.extractClaims(jwtToken);
        logger.info("JWT claims extracted: {}", claims);

        List<String> roles = claims.get("roles", List.class);
        logger.info("Roles extracted from token: {}", roles);

        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                claims.getSubject(), null, authorities
        );
        logger.info("Authentication created for user: {}", claims.getSubject());

        if (path.startsWith("/api/admin")) {
            logger.info("Admin route access requested.");

            if (roles == null || !roles.contains("ADMIN")) {
                logger.warn("Access denied. User does not have ADMIN role.");
                return forbiddenResponse(exchange);
            }

            logger.info("Admin access granted.");
        }

        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }

    private boolean isPublicRoute(String path) {
        return path.startsWith("/api/auth") || path.startsWith("/api/public");
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        logger.info("Responding with 401 UNAUTHORIZED.");
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private Mono<Void> forbiddenResponse(ServerWebExchange exchange) {
        logger.info("Responding with 403 FORBIDDEN.");
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
}
