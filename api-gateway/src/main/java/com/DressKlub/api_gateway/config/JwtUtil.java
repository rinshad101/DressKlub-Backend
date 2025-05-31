package com.DressKlub.api_gateway.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private final String SECRET_KEY;

    public JwtUtil(Dotenv dotenv) {
        this.SECRET_KEY = dotenv.get("JWT_SECRET_KEY");
        logger.info("JWTUtil initialized with secret key from environment.");
    }

    public boolean validateToken(String token) {
        try {
            logger.debug("Validating JWT token.");
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            logger.info("JWT token is valid.");
            return true;
        } catch (Exception e) {
            logger.error("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    public Claims extractClaims(String token) {
        try {
            logger.debug("Extracting claims from JWT token.");
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            logger.info("Claims extracted: Subject = {}, Roles = {}", claims.getSubject(), claims.get("roles"));
            return claims;
        } catch (Exception e) {
            logger.error("Failed to extract claims: {}", e.getMessage());
            throw e; // rethrow to let the calling class handle it
        }
    }

    public String extractTokenFromCookie(List<String> cookies) {
        logger.debug("Extracting JWT token from cookies: {}", cookies);
        Optional<String> jwtCookie = cookies.stream()
                .flatMap(cookie -> Arrays.stream(cookie.split(";")))
                .filter(c -> c.trim().startsWith("jwt="))
                .map(c -> c.split("=")[1])
                .findFirst();

        String token = jwtCookie.orElse(null);
        if (token == null) {
            logger.warn("JWT cookie not found.");
        } else {
            logger.info("JWT token extracted from cookie.");
        }
        return token;
    }
}
