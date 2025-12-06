package com.security.keycloak_app.config;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class KeycloakAuthenticationConverter  implements Converter<Jwt , Collection<GrantedAuthority>>{
    
    private final String clientId;

    public  KeycloakAuthenticationConverter(String clientId){
       this.clientId=clientId;
    }

   

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaims().get("resource_access");

        System.out.println("resourceAccess: " + resourceAccess);


        if (resourceAccess == null || !resourceAccess.containsKey(clientId)) {
            return getRealmRoles(jwt); 
        }

        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(clientId);
        Collection<String> roles = (Collection<String>) clientAccess.get("roles");

        if (roles == null) {
            return List.of();
        }
        return roles.stream()
            .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()))
            .collect(Collectors.toList());
    }
    
    private Collection<GrantedAuthority> getRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");
             return roles.stream()
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()))
                .collect(Collectors.toList());
        }
        return List.of();
    }
    
}
