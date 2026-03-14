package com.backend.billiards_management.services.keycloak;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class KeycloakUserService {

    private final Keycloak keycloak;

    private final String realm = "billiard-management";

    public String createUser(String username, String email, String password, String firstName, String lastName) {

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRequiredActions(java.util.Collections.emptyList());

        user.setCredentials(List.of(credential));

        log.info(user.getRequiredActions().size());

        Response response = keycloak.realm(realm)
                .users()
                .create(user);

        if (response.getStatus() != 201) {
            String error = response.readEntity(String.class);
            throw new RuntimeException("Create user failed. Status: "
                    + response.getStatus() + " - " + error);
        }

        String location = response.getHeaderString("Location");
        return location.substring(location.lastIndexOf("/") + 1);
    }
}