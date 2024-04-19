package io.ussopm.UserApp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtValidation {
    private final JwtProperties jwtProperties;

    public String validateTokenAndRetrieveClaims(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtProperties.getSecretKey()))
                .withSubject("Customer Details")
                .withIssuer("ussopm")
                .build();
        DecodedJWT jwt = verifier.verify(token);

        return jwt.getClaim("username").asString();
    }
}
