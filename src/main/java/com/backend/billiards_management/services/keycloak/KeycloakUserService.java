package com.backend.billiards_management.services.keycloak;

import com.backend.billiards_management.dtos.request.auth.LoginRequest;
import com.backend.billiards_management.dtos.request.auth.RefreshTokenRequest;
import com.backend.billiards_management.dtos.response.auth.AuthResponse;
import com.backend.billiards_management.dtos.response.auth.KeycloakTokenResponse;
import com.backend.billiards_management.dtos.response.employee.EmployeeRes;
import com.backend.billiards_management.entities.employee.Employee;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.EmployeeRepository;
import com.backend.billiards_management.services.employee.EmployeeService;
import com.backend.billiards_management.utils.JwtUtils;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class KeycloakUserService {

    private final Keycloak keycloak;

    private final RestTemplate restTemplate;

    private final JwtDecoder jwtDecoder;

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;


    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    public String createUser(String username,
                             String email,
                             String password,
                             String firstName,
                             String lastName) {

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
        user.setRequiredActions(Collections.emptyList());
        user.setCredentials(List.of(credential));

        Response response = keycloak.realm(realm)
                .users()
                .create(user);

        if (response.getStatus() != 201) {

            String error = response.readEntity(String.class);

            log.error("Create user failed: {}", error);

            throw new AppException(ErrorCode.FORBIDDEN ,"Create user failed. Status: " +response.getStatus() + " - " + error);
        }

        String location = response.getHeaderString("Location");
        return location.substring(location.lastIndexOf("/") + 1);
    }

    public AuthResponse login(LoginRequest request) {

        String tokenUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("grant_type", "password");
        body.add("username", request.getUsername());
        body.add("password", request.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity =
                new HttpEntity<>(body, headers);

        try {

            ResponseEntity<KeycloakTokenResponse> response =
                    restTemplate.exchange(
                            tokenUrl,
                            HttpMethod.POST,
                            entity,
                            KeycloakTokenResponse.class
                    );

            return mapToAuthResponse(response.getBody());

        } catch (HttpClientErrorException.Unauthorized ex) {

            throw new AppException(
                    ErrorCode.UNAUTHORIZED,
                    "Invalid username or password"
            );

        } catch (HttpClientErrorException ex) {

            throw new AppException(
                    ErrorCode.BAD_REQUEST,
                    "Login failed"
            );
        }
    }
    public AuthResponse refreshToken(RefreshTokenRequest request) {

        String tokenUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", request.getRefreshToken());

        // nếu client có secret thì thêm:
        // body.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity =
                new HttpEntity<>(body, headers);

        try {

            ResponseEntity<KeycloakTokenResponse> response =
                    restTemplate.exchange(
                            tokenUrl,
                            HttpMethod.POST,
                            entity,
                            KeycloakTokenResponse.class
                    );

            return mapToAuthResponse(response.getBody());

        } catch (HttpClientErrorException.Unauthorized ex) {

            throw new AppException(
                    ErrorCode.UNAUTHORIZED,
                    "Refresh token expired or invalid"
            );

        } catch (HttpClientErrorException ex) {

            throw new AppException(
                    ErrorCode.BAD_REQUEST,
                    "Refresh token failed"
            );
        }
    }


    private AuthResponse mapToAuthResponse(KeycloakTokenResponse token) {
        Map<String, Object> payload = JwtUtils.decodePayload(token.getAccessToken());

        String keycloakId = (String) payload.get("sub");
//        String username = (String) payload.get("preferred_username");
//        String email = (String) payload.get("email");
//        String name = (String) payload.get("name");

        Employee employee = (Employee) employeeRepository
                .findByKeycloakId(keycloakId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.NOT_FOUND,
                        "Cannot find employee with keycloak id: " + keycloakId
                ));

        EmployeeRes employeeRes = modelMapper.map(employee, EmployeeRes.class);


        return AuthResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .expiresIn(token.getExpiresIn())
                .employee(employeeRes)
                .build();
    }

}