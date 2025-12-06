package com.security.keycloak_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;


@Configuration
@EnableMethodSecurity
public class SecurityConfig  {
    @Bean
    public  SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(outh2 -> outh2.jwt(jwt-> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()) ));
       return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter(){
        KeycloakAuthenticationConverter keycloakAuthenticationConverter = new KeycloakAuthenticationConverter("Test-auth");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();

        jwtConverter.setJwtGrantedAuthoritiesConverter(keycloakAuthenticationConverter);
        return jwtConverter;
    }

    
}
