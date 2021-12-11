package com.hashedin.virtualproperty.application.entities;

import lombok.Data;

import javax.persistence.*;

@Table(name="property")
@Entity
@Data
public class Property
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer propertyId;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String purpose;

    @Column(nullable = false)
    private String ownerEmail;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int area;

    @Column(nullable = true)
    private int bhk;

    @Column(nullable = false)
    private int builtYear;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int floors;

    @Column(nullable = false)
    private int bedrooms;

    @Column(nullable = false)
    private int bathrooms;

    @Column(nullable = false)
    private int pinCode;

    @Column(nullable = true)
    private boolean virtualTour=false;

    @Column(nullable = true)
    private String virtualTourURL;

    public Property()
    {
    }

    public Property(String city, String state, String address, String type, String purpose, String description, int area, int bhk, int builtYear, int price, int floors, int bedrooms, int bathrooms, int pinCode,String virtualTourURL,boolean virtualTour) {
        this.city = city;
        this.state = state;
        this.address = address;
        this.type = type;
        this.purpose = purpose;
        this.description = description;
        this.area = area;
        this.bhk = bhk;
        this.builtYear = builtYear;
        this.price = price;
        this.floors = floors;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.pinCode = pinCode;
        this.virtualTour=virtualTour;
        this.virtualTourURL=virtualTourURL;
    }
}
