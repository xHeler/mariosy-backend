package com.deloitte.ads.utils;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.factories.EmployeeDtoFactory;
import com.deloitte.ads.services.EmployeeCreationService;
import com.deloitte.ads.services.EmployeeRetrievalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final EmployeeCreationService employeeCreationService;
    private final EmployeeRetrievalService employeeRetrievalService;

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        log.info("Converting JWT to AuthenticationToken...");
        Collection<GrantedAuthority> authorities = extractRoles(source);

        if (!isEmployeeAlreadyExist(source)) {
            log.info("Employee does not exist. Creating new employee...");
            createUserFromJwtTokenInformation(source);
        }

        String name = source.getClaim(JwtClaimNames.SUB);
        log.info("Converted JWT to AuthenticationToken for user: {}", name);
        return new JwtAuthenticationToken(source, authorities, name);
    }

    private Collection<GrantedAuthority> extractRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) {
            return Collections.emptySet();
        }

        Map<String, Object> resource = (Map<String, Object>) resourceAccess.get("simple-rest-api");
        if (resource == null) {
            return Collections.emptySet();
        }

        Collection<String> roles = (Collection<String>) resource.get("roles");
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

    private void createUserFromJwtTokenInformation(Jwt jwt) {
        String firstName = jwt.getClaim("given_name");
        String lastName = jwt.getClaim("family_name");
        String email = jwt.getClaim("email");
        String employeeId = jwt.getClaim("sub");
        log.info("Created new employee from JWT information. Employee ID: {}", employeeId);
        EmployeeDto employee = EmployeeDtoFactory.createEmployeeDto(email, firstName, lastName);
        employeeCreationService.saveEmployee(employeeId, employee);
    }

    private boolean isEmployeeAlreadyExist(Jwt jwt) {
        String employeeId = jwt.getClaim("sub");
        log.info("Checking if employee already exists. Employee ID: {}", employeeId);
        return employeeRetrievalService.isEmployeeExist(employeeId);
    }
}