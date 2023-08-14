package com.deloitte.ads.utils;

import com.deloitte.ads.dto.*;
import com.deloitte.ads.factories.KeycloakRequestFactory;
import com.deloitte.ads.factories.KeycloakUrlFactory;
import com.deloitte.ads.services.EmployeeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakAuth {

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.base_url}")
    private String baseUrl;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${keycloak.reals_name}")
    private String realsName;

    private final EmployeeService employeeService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    private TokenDto getAdminAccessToken() {
        UserLoginDto adminDto = UserLoginDto.builder().email(adminUsername).password(adminPassword).build();
        ResponseEntity<TokenDto> response = authenticateWithKeycloak("master", "admin-cli", adminDto);
        return response.getBody();
    }

    public ResponseEntity<TokenDto> authenticateWithKeycloak(UserLoginDto userDto) {
        return authenticateWithKeycloak(this.realsName, this.clientId, userDto);
    }

    public ResponseEntity<TokenDto> authenticateWithKeycloak(String realsName, String clientId, UserLoginDto userDto) {
        log.info("Authenticate to Keycloak Realms: {},  with EmployeeDto: {}", realsName, userDto);

        HttpEntity<MultiValueMap<String, String>> request = KeycloakRequestFactory.buildAuthenticationRequest(clientId, userDto);
        String authUrl = KeycloakUrlFactory.getAuthenticationUrl(this.baseUrl, realsName);
        log.info("Authenticate to auth url: {}", authUrl);

        ResponseEntity<TokenDto> response = restTemplate.postForEntity(authUrl, request, TokenDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<String> registerUser(EmployeeDto dto) {
        TokenDto adminToken = getAdminAccessToken();
        if (adminToken == null || adminToken.getAccess_token() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to obtain admin access token");
        }

        HttpEntity<UserDto> request = KeycloakRequestFactory.buildRegisterUserRequest(adminToken, dto);
        String registerUrl = KeycloakUrlFactory.getRegisterUserUrl(this.baseUrl, this.realsName);
        log.info("Register url: {}", registerUrl);

        ResponseEntity<String> response = restTemplate.postForEntity(registerUrl, request, String.class);
        employeeService.saveEmployee(dto);

        String userId = findUserIdByEmail(dto.getEmail());
        log.info("Found user id: {}", userId);
        setNewPassword(userId, dto.getPassword());
        return response;
    }

    @Transactional
    public ResponseEntity<String> setNewPassword(String userId, String newPassword) {
        TokenDto adminToken = getAdminAccessToken();
        if (adminToken == null || adminToken.getAccess_token() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to obtain admin access token");
        }

        HttpEntity<CredentialDto> request = KeycloakRequestFactory.buildPasswordResetRequest(adminToken, newPassword);
        String passwordResetUrl = KeycloakUrlFactory.getPasswordResetUrl(this.baseUrl, this.realsName, userId);
        log.info("Password reset URL: {}", passwordResetUrl);

        ResponseEntity<String> response = restTemplate.exchange(passwordResetUrl, HttpMethod.PUT, request, String.class);
        return response;
    }

    public String findUserIdByEmail(String email) {
        TokenDto adminToken = getAdminAccessToken();
        if (adminToken == null || adminToken.getAccess_token() == null) {
            return null;
        }

        HttpEntity<?> request = KeycloakRequestFactory.buildUserSearchRequest(adminToken);
        String userSearchUrl = KeycloakUrlFactory.getUserSearchUrl(this.baseUrl, this.realsName, email);
        log.info("User search URL: {}", userSearchUrl);

        ResponseEntity<String> response = restTemplate.exchange(userSearchUrl, HttpMethod.GET, request, String.class);
        log.info("Response from search user: {}", response.getBody());

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            try {
                JsonNode usersNode = objectMapper.readTree(response.getBody());
                if (usersNode.isArray() && usersNode.size() > 0) {
                    JsonNode firstUserNode = usersNode.get(0);
                    return firstUserNode.get("id").asText();
                }
            } catch (Exception e) {
                log.error("Error parsing JSON response: {}", e.getMessage());
            }
        }
        return null;
    }
}
