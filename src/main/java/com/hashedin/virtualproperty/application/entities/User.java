package com.hashedin.virtualproperty.application.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_table")
@Data // Lombok will provide all the getter and setter methods
public class User {
    // the user table will have email as primary key
    @Id
    private String email;

    // bcrypt hashed password will be stored for future authentication
    @Column(nullable = false)
    private String password;

    // name of the user
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = true)
    private String userImage;

    @Column(nullable = false, unique = true)
    private String mobile;

    @Column()
    private boolean verified;

    @Column()
    private boolean administrator;

    public User() {
    } // default constructor as required by JPA

    public User(String email, String password, String name, String mobile, String address) {
        // parameterized constructor for ease of creating objects
        this.email = email;
        this.name = name;
        this.password = password;
        this.mobile = mobile;
        this.address = address;
    }
}
