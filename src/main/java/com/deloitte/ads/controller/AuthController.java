package com.deloitte.ads.controller;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.dto.TokenDto;
import com.deloitte.ads.dto.UserDto;
import com.deloitte.ads.dto.UserLoginDto;
import com.deloitte.ads.utils.KeycloakAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final KeycloakAuth keycloakAuth;

    @PostMapping("/login")
    ResponseEntity<TokenDto> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        log.info("Login with credentials: {}", userLoginDto);
        return keycloakAuth.authenticateWithKeycloak(userLoginDto);
    }

    @PostMapping("/register")
    ResponseEntity<String> register(@RequestBody @Valid EmployeeDto employeeDto) {
        log.info("Register new user: {}", employeeDto);
        return keycloakAuth.registerUser(employeeDto);
    }
}
