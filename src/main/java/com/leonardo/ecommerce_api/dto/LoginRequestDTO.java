package com.leonardo.ecommerce_api.dto;

import jakarta.validation.constraints.Email;

public class LoginRequestDTO {

    @Email
    private String email;
    
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
