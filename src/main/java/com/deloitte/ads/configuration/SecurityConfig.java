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
import java.lang.annotation.Annotation;
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
        configureSecureEndpoints(http);
        http.authorizeRequests().anyRequest().permitAll();
    }

    private void configureSecureEndpoints(HttpSecurity http) throws Exception {
        secureEndpointsMap.forEach((requestMethod, endpoints) -> {
            HttpMethod httpMethod = HttpMethod.resolve(requestMethod.name());
            endpoints.forEach(endpoint -> {
                try {
                    http.authorizeRequests().antMatchers(httpMethod, "/" + endpoint).authenticated();
                } catch (Exception e) {
                    log.error("Error configuring endpoint: {}", endpoint, e);
                }
            });
        });
    }

    private void setupSession(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @PostConstruct
    public void init() {
        secureEndpointsMap = getSecureEndpointsMap();
        log.info("Secure endpoints: {}", secureEndpointsMap);
    }

    private Map<RequestMethod, List<String>> getSecureEndpointsMap() {
        Map<RequestMethod, List<String>> secureEndpointsMap = new HashMap<>();
        context.getBeansWithAnnotation(RestController.class).values()
                .forEach(bean -> updateSecureEndpointsMapForController(secureEndpointsMap, bean));
        return secureEndpointsMap;
    }

    private void updateSecureEndpointsMapForController(Map<RequestMethod, List<String>> map, Object bean) {
        Class<?> controllerClass = AopProxyUtils.ultimateTargetClass(bean);
        String basePath = getBasePath(controllerClass);
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(SecureEndpoint.class)) {
                handleAnnotationsForSecureEndpoints(map, method, basePath);
            }
        }
    }

    private String getBasePath(Class<?> controllerClass) {
        RequestMapping mapping = AnnotatedElementUtils.findMergedAnnotation(controllerClass, RequestMapping.class);
        return (mapping != null && mapping.value().length > 0) ? mapping.value()[0] : "";
    }

    private void handleAnnotationsForSecureEndpoints(Map<RequestMethod, List<String>> map, Method method, String basePath) {
        Class<?>[] annotations = {GetMapping.class, PostMapping.class, PutMapping.class, DeleteMapping.class, PatchMapping.class};
        for (Class<?> annotationClass : annotations) {
            Annotation annotation = AnnotatedElementUtils.findMergedAnnotation(method, (Class<Annotation>) annotationClass);
            if (annotation != null) {
                RequestMethod requestMethod = getRequestMethodForAnnotation(annotationClass);
                updateEndpointMap(map, requestMethod, basePath, getPathsFromAnnotation(annotation));
            }
        }
    }

    private RequestMethod getRequestMethodForAnnotation(Class<?> annotationClass) {
        if (annotationClass == GetMapping.class) return RequestMethod.GET;
        if (annotationClass == PostMapping.class) return RequestMethod.POST;
        if (annotationClass == PutMapping.class) return RequestMethod.PUT;
        if (annotationClass == DeleteMapping.class) return RequestMethod.DELETE;
        if (annotationClass == PatchMapping.class) return RequestMethod.PATCH;
        throw new IllegalArgumentException("Unsupported annotation: " + annotationClass);
    }

    private String[] getPathsFromAnnotation(Annotation annotation) {
        if (annotation instanceof GetMapping) return ((GetMapping) annotation).value();
        if (annotation instanceof PostMapping) return ((PostMapping) annotation).value();
        if (annotation instanceof PutMapping) return ((PutMapping) annotation).value();
        if (annotation instanceof DeleteMapping) return ((DeleteMapping) annotation).value();
        if (annotation instanceof PatchMapping) return ((PatchMapping) annotation).value();
        throw new IllegalArgumentException("Unsupported annotation: " + annotation);
    }

    private void updateEndpointMap(Map<RequestMethod, List<String>> map, RequestMethod requestMethod, String basePath, String[] paths) {
        String pathToUse = paths.length > 0 ? basePath + paths[0] : basePath;
        map.computeIfAbsent(requestMethod, k -> new ArrayList<>()).add(pathToUse);
    }
}