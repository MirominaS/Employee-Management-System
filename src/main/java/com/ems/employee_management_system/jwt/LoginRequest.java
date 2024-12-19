package com.ems.employee_management_system.jwt;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "User name should not be empty")
    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
