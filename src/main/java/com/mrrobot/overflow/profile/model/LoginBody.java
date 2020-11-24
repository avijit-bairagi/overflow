package com.mrrobot.overflow.profile.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginBody {
    @NotBlank
    @Size(min = 3, max = 20, message = "username must be between 3 to 20 characters")
    private String username;

    @NotBlank
    @Size(min = 6, max = 20, message = "password must be between 6 to 20 characters")
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