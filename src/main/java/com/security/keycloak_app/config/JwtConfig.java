package com.security.keycloak_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

@Configuration
public class JwtConfig {
    @Value("${spring.security.oauth2.resourceServer.jwt.issuer-uri}")
    private String issuerUri;

     public JwtDecoder jwtDecoder(){
         NimbusJwtDecoder decoder  = JwtDecoders.fromIssuerLocation(issuerUri);

         OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
         OAuth2TokenValidator<Jwt>  customValidator = new CustomIssuerValidator();
         OAuth2TokenValidator<Jwt>   validator  =new DelegatingOAuth2TokenValidator<>(withIssuer, customValidator);

         decoder.setJwtValidator(validator);
         return decoder;
     }
}
