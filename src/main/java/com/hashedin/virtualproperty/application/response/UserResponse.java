package com.hashedin.virtualproperty.application.response;

public class UserResponse {

    public String name;
    public String address;
    public String userImage;
    public String mobile;
    public String email;
    public boolean isAdministrator;

    public UserResponse(String name, String address, String userImage, String mobile, String email,boolean isAdministrator) {
        this.name = name;
        this.address = address;
        this.userImage = userImage;
        this.mobile = mobile;
        this.email = email;
        this.isAdministrator=isAdministrator;
    }
}
