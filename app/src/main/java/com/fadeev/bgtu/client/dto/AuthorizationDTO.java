package com.fadeev.bgtu.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationDTO {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;

    public AuthorizationDTO(){ }

    public String getPassword() { return password; }
    public String getUsername() { return username; }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) { this.username = username; }
}
