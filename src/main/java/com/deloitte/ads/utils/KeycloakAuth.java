package com.deloitte.ads.utils;

import com.deloitte.ads.dto.*;
import com.deloitte.ads.services.EmployeeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
        adminDto.setEmail(adminUsername);
        adminDto.setPassword(adminPassword);
        return authenticateWithKeycloak("master", "admin-cli", adminDto).getBody();
    }

    public ResponseEntity<TokenDto> authenticateWithKeycloak(UserLoginDto userDto) {
        return this.authenticateWithKeycloak(this.realsName, this.clientId, userDto);
    }

    public ResponseEntity<TokenDto> authenticateWithKeycloak(String realsName, String clientId, UserLoginDto userDto) {
        log.info("Authenticate to Keycloak Realms: {},  with EmployeeDto: {}", realsName, userDto);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("username", userDto.getEmail());
        map.add("password", userDto.getPassword());
        map.add("grant_type", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String authUrl = this.baseUrl + "/realms/" + realsName + "/protocol/openid-connect/token";
        log.info("Authenticate to auth url: {}", authUrl);

        ResponseEntity<TokenDto> response = restTemplate.postForEntity(
                authUrl,
                request, TokenDto.class
        );

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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken.getAccess_token());

        UserDto userDto = UserDto.builder().email(dto.getEmail()).enabled(true).build();
        HttpEntity<UserDto> request = new HttpEntity<>(userDto, headers);
        String registerUrl = this.baseUrl + "/admin/realms/" + this.realsName + "/users";
        log.info("Register url: {}", registerUrl);
        ResponseEntity<String> response = restTemplate.exchange(
                registerUrl,
                HttpMethod.POST,
                request,
                String.class,
                this.realsName
        );
        employeeService.saveEmployee(dto);
        String userId = this.findUserIdByEmail(dto.getEmail());
        log.info("Found user id: {}", userId);
        this.setNewPassword(userId, dto.getPassword());
        return response;
    }

    @Transactional
    public ResponseEntity<String> setNewPassword(String userId, String newPassword) {
        TokenDto adminToken = getAdminAccessToken();
        if (adminToken == null || adminToken.getAccess_token() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to obtain admin access token");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken.getAccess_token());

        CredentialDto credentialRepresentation = CredentialDto.builder().value(newPassword).build();

        HttpEntity<CredentialDto> request = new HttpEntity<>(credentialRepresentation, headers);
        String passwordResetUrl = this.baseUrl + "/admin/realms/" + this.realsName + "/users/" + userId + "/reset-password";
        log.info("Password reset URL: {}", passwordResetUrl);

        ResponseEntity<String> response = restTemplate.exchange(
                passwordResetUrl,
                HttpMethod.PUT,
                request,
                String.class
        );

        return response;
    }

    public String findUserIdByEmail(String email) {
        TokenDto adminToken = getAdminAccessToken();
        if (adminToken == null || adminToken.getAccess_token() == null) {
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken.getAccess_token());

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/admin/realms/" + realsName + "/users")
                .queryParam("email", email)
                .queryParam("exact", true);

        HttpEntity<?> request = new HttpEntity<>(headers);
        String userSearchUrl = uriBuilder.toUriString();
        log.info("User search URL: {}", userSearchUrl);

        ResponseEntity<String> response = restTemplate.exchange(
                userSearchUrl,
                HttpMethod.GET,
                request,
                String.class
        );

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
