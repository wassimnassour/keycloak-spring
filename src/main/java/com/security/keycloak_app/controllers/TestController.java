package com.security.keycloak_app.controllers;

import com.sun.security.auth.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/")
class TestController {

    @GetMapping("/get")
    @PreAuthorize("hasRole('ROLE_REALME_ROLES_SUPER_ADMIN1')")
    public ResponseEntity<String> getMethodName() {
        return new ResponseEntity<String>("This is Get param THIS_ISMS_DEPAREMENT_ROLE", HttpStatus.OK);
    }


    @GetMapping("/get2")
    @PreAuthorize("hasRole('ROLE_REALME_ROLES_SUPER_ADMIN2')")
    public ResponseEntity<String> getMethodName2() {
        return new ResponseEntity<String>("This is Get param THIS_ISMS_DEPAREMENT_ROLE", HttpStatus.OK);
    }

    @GetMapping("/userinfo")
    public ResponseEntity<Map<String, Object>>getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        response.put("username", jwt.getClaim("preferred_username"));
        response.put("email", jwt.getClaim("email"));
        response.put("given_name", jwt.getClaim("given_name"));
        response.put("family_name", jwt.getClaim("family_nam    e"));
        response.put("roles", jwt.getClaimAsStringList("realm_access"));


        return ResponseEntity.ok(response);

    }

}