package com.deloitte.ads.factories;

public class KeycloakUrlFactory {

    public static String getAuthenticationUrl(String baseUrl, String realsName) {
        return baseUrl + "/realms/" + realsName + "/protocol/openid-connect/token";
    }

    public static String getRegisterUserUrl(String baseUrl, String realsName) {
        return baseUrl + "/admin/realms/" + realsName + "/users";
    }

    public static String getPasswordResetUrl(String baseUrl, String realsName, String userId) {
        return baseUrl + "/admin/realms/" + realsName + "/users/" + userId + "/reset-password";
    }

    public static String getUserSearchUrl(String baseUrl, String realsName, String email) {
        return baseUrl + "/admin/realms/" + realsName + "/users?email=" + email + "&exact=true";
    }
}
