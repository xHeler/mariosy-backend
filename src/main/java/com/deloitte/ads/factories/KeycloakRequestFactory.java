package com.deloitte.ads.factories;

import com.deloitte.ads.dto.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class KeycloakRequestFactory {

    public static HttpEntity<MultiValueMap<String, String>> buildAuthenticationRequest(String clientId, UserLoginDto userDto) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("username", userDto.getEmail());
        map.add("password", userDto.getPassword());
        map.add("grant_type", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(map, headers);
    }

    public static HttpEntity<UserDto> buildRegisterUserRequest(TokenDto adminToken, EmployeeDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken.getAccess_token());

        UserDto userDto = UserDto.builder().email(dto.getEmail()).enabled(true).build();
        return new HttpEntity<>(userDto, headers);
    }

    public static HttpEntity<CredentialDto> buildPasswordResetRequest(TokenDto adminToken, String newPassword) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken.getAccess_token());

        CredentialDto credentialRepresentation = CredentialDto.builder().value(newPassword).build();
        return new HttpEntity<>(credentialRepresentation, headers);
    }

    public static HttpEntity<?> buildUserSearchRequest(TokenDto adminToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken.getAccess_token());
        return new HttpEntity<>(headers);
    }
}
