package com.hashedin.virtualproperty.application.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {
    // Login will require only email and password
    public String email;
    public String password;
}
