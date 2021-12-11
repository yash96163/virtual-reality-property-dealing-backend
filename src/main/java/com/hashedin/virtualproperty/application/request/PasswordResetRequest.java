package com.hashedin.virtualproperty.application.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordResetRequest {
    public String token;
    public String password;
}
