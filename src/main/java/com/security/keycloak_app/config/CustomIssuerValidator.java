package com.security.keycloak_app.config;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class CustomIssuerValidator implements OAuth2TokenValidator<Jwt> {


    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        String azp = token.getClaim("azp");
        if (!azp.equals("front-end-app")) {
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Invalid client", null));
        } else {
            return OAuth2TokenValidatorResult.success();
        }

    }
}
