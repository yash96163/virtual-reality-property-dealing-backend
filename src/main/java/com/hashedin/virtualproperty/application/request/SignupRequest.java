package com.hashedin.virtualproperty.application.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignupRequest {
    // signup will require email, password and name of the user
    public String email;
    public String password;
    public String name;
    public String address;
    public String mobile;

}
