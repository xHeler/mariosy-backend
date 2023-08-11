package com.deloitte.ads.utils;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakAuth {

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String url;

    public TokenDto authenticateWithKeycloak(EmployeeDto dto) {
        log.info("Authenticate to Keycloak with EmployeeDto: {}", dto);
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", this.clientId);
        map.add("username", dto.getEmail());
        map.add("password", dto.getPassword());
        map.add("grant_type", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String authUrl = this.url + "/protocol/openid-connect/token";

        ResponseEntity<TokenDto> response = restTemplate.postForEntity(
                authUrl,
                request, TokenDto.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }
}
