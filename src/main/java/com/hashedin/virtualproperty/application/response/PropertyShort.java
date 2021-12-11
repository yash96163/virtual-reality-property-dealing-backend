package com.hashedin.virtualproperty.application.response;

import lombok.Data;

@Data
public class PropertyShort {
    public Integer propertyId;
    public String city;
    public String state;
    public String address;
    public String purpose;
    public int area;
    public int bhk;
    public int price;
    public int floors;
    public int bedrooms;
    public int bathrooms;
    public int pinCode;
    public String image;
    public int built_year;
    public String type;
    public String ownerEmail;
    public boolean virtualTour;
    public String virtualTourUrl;

    public PropertyShort(Integer propertyId, String city, String state, String address, int area, int bhk, int price, int floors, int bedrooms, int bathrooms, int pinCode, String image,String purpose,int built_year,String type,String ownerEmail,boolean virtualTour,String virtualTourUrl) {
        this.propertyId = propertyId;
        this.city = city;
        this.state = state;
        this.address = address;
        this.area = area;
        this.bhk = bhk;
        this.price = price;
        this.floors = floors;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.pinCode = pinCode;
        this.image = image;
        this.purpose=purpose;
        this.built_year=built_year;
        this.type=type;
        this.ownerEmail=ownerEmail;
        this.virtualTour=virtualTour;
        this.virtualTourUrl=virtualTourUrl;
    }
}
