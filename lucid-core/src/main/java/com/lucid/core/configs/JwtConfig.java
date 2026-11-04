package com.lucid.core.configs;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationms}")
    private long expirationMs;

    public String getJwtSecret() {
        return jwtSecret;
    }

    public long getExpirationMs() {
        return expirationMs;
    }
}
