package com.springboot.rest.payload;

public class JwtResponse {

    private final String username;
    private final String token;

    public JwtResponse(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
