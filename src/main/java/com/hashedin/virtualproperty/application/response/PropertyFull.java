package com.hashedin.virtualproperty.application.response;

import lombok.Data;

import java.util.List;

@Data
public class PropertyFull {
  public Integer propertyId;

  public String city;

  public String state;

  public String address;

  public String type;

  public String purpose;

  public String ownerEmail;

  public String description;

  public int area;

  public int bhk;

  public int builtYear;

  public int price;

  public int floors;

  public int bedrooms;

  public int bathrooms;

  public int pinCode;

  public List<Image> images;

  public boolean virtualTour=false;

  public String virtualTourUrl="";

  public PropertyFull(
      Integer propertyId,
      String city,
      String state,
      String address,
      String type,
      String purpose,
      String ownerEmail,
      String description,
      int area,
      int bhk,
      int builtYear,
      int price,
      int floors,
      int bedrooms,
      int bathrooms,
      int pinCode,
      List<Image> propertyImages,
      boolean virtualTour,
      String virtualTourUrl) {
    this.propertyId = propertyId;
    this.city = city;
    this.state = state;
    this.address = address;
    this.type = type;
    this.purpose = purpose;
    this.ownerEmail = ownerEmail;
    this.description = description;
    this.area = area;
    this.bhk = bhk;
    this.builtYear = builtYear;
    this.price = price;
    this.floors = floors;
    this.bedrooms = bedrooms;
    this.bathrooms = bathrooms;
    this.pinCode = pinCode;
    this.images = propertyImages;
    this.virtualTour=virtualTour;
    this.virtualTourUrl=virtualTourUrl;
  }
}
