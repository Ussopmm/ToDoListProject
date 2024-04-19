package io.ussopm.AuthService.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtIssuer {

    private final JwtProperties jwtProperties;
    public String issue(String username) {
        return JWT.create()
                .withSubject("Customer Details")
                .withIssuer("ussopm")
                .withIssuedAt(new Date())
                .withExpiresAt(Instant.now().plus(Duration.of(60, ChronoUnit.MINUTES)))
                .withClaim("username", username)
                .sign(Algorithm.HMAC256(jwtProperties.getSecretKey()));
    }
}
