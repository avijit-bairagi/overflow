package com.mrrobot.overflow.security.model;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;

    public JwtResponse(String accessToken, Long id) {
        this.token = accessToken;
        this.id = id;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}