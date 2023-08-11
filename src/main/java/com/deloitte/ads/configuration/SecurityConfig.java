package com.deloitte.ads.configuration;

import com.deloitte.ads.utils.JwtConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final ApplicationContext context;
    private final JwtConverter jwtConverter;
    private Map<RequestMethod, List<String>> secureEndpointsMap;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        setupSecurity(http);
        setupAuthentication(http);
        setupSession(http);
    }

    private void setupAuthentication(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtConverter);
    }

    private void setupSecurity(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        for (Map.Entry<RequestMethod, List<String>> entry : secureEndpointsMap.entrySet()) {
            HttpMethod httpMethod = HttpMethod.resolve(entry.getKey().name());
            for (String endpoint : entry.getValue()) {
                http
                        .authorizeRequests()
                        .antMatchers(httpMethod, "/" + endpoint)
                        .authenticated();
            }
        }
        http.authorizeRequests().anyRequest().permitAll();
    }

    private void setupSession(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @PostConstruct
    public void init() {
        secureEndpointsMap = getSecureEndpointsMap();
        log.info("Secure endpoints: " + secureEndpointsMap);
    }

    private Map<RequestMethod, List<String>> getSecureEndpointsMap() {
        Map<RequestMethod, List<String>> secureEndpointsMap = new HashMap<>();

        Map<String, Object> beans = context.getBeansWithAnnotation(
                RestController.class
        );

        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Class<?> controllerClass = AopProxyUtils.ultimateTargetClass(
                    entry.getValue()
            );
            String basePath = getBasePath(controllerClass);

            secureEndpointsMap =
                    handleControllerMethods(
                            secureEndpointsMap,
                            controllerClass,
                            basePath
                    );
        }

        return secureEndpointsMap;
    }

    private String getBasePath(Class<?> controllerClass) {
        RequestMapping controllerRequestMapping = AnnotatedElementUtils.findMergedAnnotation(
                controllerClass,
                RequestMapping.class
        );
        return (
                controllerRequestMapping != null &&
                        controllerRequestMapping.value().length > 0
        )
                ? controllerRequestMapping.value()[0]
                : "";
    }

    private Map<RequestMethod, List<String>> handleControllerMethods(
            Map<RequestMethod, List<String>> secureEndpointsMap,
            Class<?> controllerClass,
            String basePath
    ) {
        Method[] methods = controllerClass.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(SecureEndpoint.class)) {
                secureEndpointsMap =
                        handleMethodAnnotations(
                                secureEndpointsMap,
                                method,
                                basePath
                        );
            }
        }

        return secureEndpointsMap;
    }

    private Map<RequestMethod, List<String>> handleMethodAnnotations(
            Map<RequestMethod, List<String>> secureEndpointsMap,
            Method method,
            String basePath
    ) {
        secureEndpointsMap =
                handleGetMapping(secureEndpointsMap, method, basePath);
        secureEndpointsMap =
                handlePostMapping(secureEndpointsMap, method, basePath);
        secureEndpointsMap =
                handlePutMapping(secureEndpointsMap, method, basePath);
        secureEndpointsMap =
                handleDeleteMapping(secureEndpointsMap, method, basePath);
        secureEndpointsMap =
                handlePatchMapping(secureEndpointsMap, method, basePath);

        return secureEndpointsMap;
    }

    private Map<RequestMethod, List<String>> handleGetMapping(
            Map<RequestMethod, List<String>> secureEndpointsMap,
            Method method,
            String basePath
    ) {
        GetMapping getMapping = AnnotatedElementUtils.findMergedAnnotation(
                method,
                GetMapping.class
        );
        if (getMapping != null) {
            secureEndpointsMap =
                    updateEndpointMap(
                            secureEndpointsMap,
                            RequestMethod.GET,
                            basePath,
                            getMapping.value()
                    );
        }
        return secureEndpointsMap;
    }

    private Map<RequestMethod, List<String>> handlePostMapping(
            Map<RequestMethod, List<String>> secureEndpointsMap,
            Method method,
            String basePath
    ) {
        PostMapping postMapping = AnnotatedElementUtils.findMergedAnnotation(
                method,
                PostMapping.class
        );
        if (postMapping != null) {
            secureEndpointsMap =
                    updateEndpointMap(
                            secureEndpointsMap,
                            RequestMethod.POST,
                            basePath,
                            postMapping.value()
                    );
        }
        return secureEndpointsMap;
    }

    private Map<RequestMethod, List<String>> handlePutMapping(
            Map<RequestMethod, List<String>> secureEndpointsMap,
            Method method,
            String basePath
    ) {
        PutMapping putMapping = AnnotatedElementUtils.findMergedAnnotation(
                method,
                PutMapping.class
        );
        if (putMapping != null) {
            secureEndpointsMap =
                    updateEndpointMap(
                            secureEndpointsMap,
                            RequestMethod.PUT,
                            basePath,
                            putMapping.value()
                    );
        }
        return secureEndpointsMap;
    }

    private Map<RequestMethod, List<String>> handleDeleteMapping(
            Map<RequestMethod, List<String>> secureEndpointsMap,
            Method method,
            String basePath
    ) {
        DeleteMapping deleteMapping = AnnotatedElementUtils.findMergedAnnotation(
                method,
                DeleteMapping.class
        );
        if (deleteMapping != null) {
            secureEndpointsMap =
                    updateEndpointMap(
                            secureEndpointsMap,
                            RequestMethod.DELETE,
                            basePath,
                            deleteMapping.value()
                    );
        }
        return secureEndpointsMap;
    }

    private Map<RequestMethod, List<String>> handlePatchMapping(
            Map<RequestMethod, List<String>> secureEndpointsMap,
            Method method,
            String basePath
    ) {
        PatchMapping patchMapping = AnnotatedElementUtils.findMergedAnnotation(
                method,
                PatchMapping.class
        );
        if (patchMapping != null) {
            secureEndpointsMap =
                    updateEndpointMap(
                            secureEndpointsMap,
                            RequestMethod.PATCH,
                            basePath,
                            patchMapping.value()
                    );
        }
        return secureEndpointsMap;
    }

    private Map<RequestMethod, List<String>> updateEndpointMap(
            Map<RequestMethod, List<String>> secureEndpointsMap,
            RequestMethod requestMethod,
            String basePath,
            String[] paths
    ) {
        if (paths.length > 0) {
            secureEndpointsMap
                    .computeIfAbsent(requestMethod, k -> new ArrayList<>())
                    .add(basePath + paths[0]);
        } else {
            secureEndpointsMap
                    .computeIfAbsent(requestMethod, k -> new ArrayList<>())
                    .add(basePath);
        }
        return secureEndpointsMap;
    }
}