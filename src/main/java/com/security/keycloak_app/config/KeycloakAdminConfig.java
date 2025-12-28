package com.security.keycloak_app.config;

import dto.request.CreateUserDto;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Configuration
public class KeycloakAdminConfig {

    @Value("${keycloakAdmin.clientId}")
    private String clientId;

    @Value("${keycloakAdmin.clientSecret}")
    private String clientSecret;

    @Value("${keycloakAdmin.serverUrl}")
    private String serverUrl;

    @Value("${keycloakAdmin.realm}")
    private String realm;

    private Keycloak keycloak;

    @PostConstruct
    public void init() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master") // admin authentication realm
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("client_credentials")
                .build();
    }

    public String createUser(CreateUserDto userDto) {

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();


        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setEnabled(true);
        user.setEmailVerified(true);

        Response response = usersResource.create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user: " + response.getStatus());
        }

        String userId = response.getLocation()
                .getPath()
                .replaceAll(".*/([^/]+)$", "$1");

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userDto.getPassword());
        credential.setTemporary(false);

        usersResource.get(userId).resetPassword(credential);

        return userId;
    }


    public void assignClientRoleToUser( String RoleName ,  String userId) {
        ClientRepresentation client = keycloak.realm(realm).clients().findByClientId(clientId).get(0);

        RoleRepresentation role = keycloak.realm(realm)
                .clients()
                .get(client.getId())
                .roles()
                .get(RoleName)
                .toRepresentation();

        keycloak.realm(realm).users().get(userId).roles().clientLevel(client.getId()).add(List.of(role));

    }

    public void removeClientRoleFromUser(String RoleName, String userId) {
        ClientRepresentation client = keycloak.realm(realm).clients().findByClientId(clientId).get(0);

        RoleRepresentation role = keycloak.realm(realm)
                .clients()
                .get(client.getId())
                .roles()
                .get(RoleName)
                .toRepresentation();

        keycloak.realm(realm).users().get(userId).roles().clientLevel(client.getId()).remove(List.of(role));

    }
}
