package com.fadeev.bgtu.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenAndNameDTO {
    @JsonProperty("username")
    private String username;
    @JsonProperty("token")
    private String token;

    public TokenAndNameDTO(){}

    public String getUsername() { return username; }
    public String getToken() { return token; }
    public void setUsername(String username) { this.username = username; }
    public void setToken(String token) { this.token = token; }
}
