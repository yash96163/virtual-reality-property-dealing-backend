package com.hashedin.virtualproperty.application.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PropertyRequest {
  public String city;
  public String state;
  public String address;
  public String type;
  public String purpose;
  public String description;
  public boolean virtualTour;
  public int area;
  public int bhk;
  public int builtYear;
  public int price;
  public int floors;
  public int bedrooms;
  public int bathrooms;
  public int pinCode;


}
