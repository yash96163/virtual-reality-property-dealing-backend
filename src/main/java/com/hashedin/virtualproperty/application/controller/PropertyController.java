package com.hashedin.virtualproperty.application.controller;

import com.hashedin.virtualproperty.application.entities.Property;
import com.hashedin.virtualproperty.application.request.PropertyRequest;
import com.hashedin.virtualproperty.application.response.PropertyFull;
import com.hashedin.virtualproperty.application.response.PropertyResponse;
import com.hashedin.virtualproperty.application.response.PropertyShort;
import com.hashedin.virtualproperty.application.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/property")
@RestController
public class PropertyController {
  @Autowired private PropertyService propertyService;

  @PostMapping("")
  public PropertyFull post(
      @RequestBody PropertyRequest property,
      @RequestHeader(name = "Authorization", defaultValue = "") String token) {
    // we are using PropertyRequest instead of Property in RequestBody
    // to make Swagger not show Id and ownerEmail required in body
    return propertyService.createProperty(property, token);
  }

  @GetMapping("")
  public PropertyResponse get(
      @RequestParam(required = false, defaultValue = "0") int minPrice,
      @RequestParam(required = false, defaultValue = "2147483647") int maxPrice,
      @RequestParam(required = false, defaultValue = "") String street,
      @RequestParam(required = false, defaultValue = "") String city,
      @RequestParam(required = false, defaultValue = "") String state,
      @RequestParam(required = false, defaultValue = "") String type,
      @RequestParam(required = false, defaultValue = "") String purpose,
      @RequestParam(defaultValue = "1") int page) {
    return propertyService.getProperty(minPrice, maxPrice, street, city, state, type, purpose, page);
  }

  @GetMapping("/{propertyId}")
  public PropertyFull getById(@PathVariable Integer propertyId) {
    return propertyService.getPropertyById(propertyId);
  }

  @GetMapping("/owner/{ownerEmail}")
  public PropertyResponse getOwnerProperty(@PathVariable String ownerEmail, @RequestParam(defaultValue = "1") int page) {
    return propertyService.getOwnerProperty(ownerEmail, page);
  }

  @PatchMapping("/{id}")
  public PropertyFull patch(
      @RequestBody PropertyRequest property,
      @PathVariable Integer id,
      @RequestHeader(name = "Authorization", defaultValue = "") String token) {
    return propertyService.editProperty(property, id, token);
  }

  @DeleteMapping("/{id}")
  public Property delete(
      @PathVariable Integer id,
      @RequestHeader(name = "Authorization", defaultValue = "") String token) {
    return propertyService.deleteProperty(id, token);
  }

  @PostMapping("/{propertyId}/image")
  public PropertyFull addImage(
      @PathVariable Integer propertyId,
      @RequestParam("image") MultipartFile image,
      @RequestHeader(required = false, defaultValue = "", name = "Authorization") String token)
      throws IOException {
    return this.propertyService.addImage(propertyId, image, token);
  }

  @DeleteMapping("/image/{imageId}")
  public PropertyFull deleteImage(
      @PathVariable String imageId,
      @RequestHeader(defaultValue = "", name = "Authorization") String token)
      throws Exception {
    return this.propertyService.deleteImage(imageId, token);
  }

  @GetMapping("/virtualTour")
  public PropertyResponse getProperty( @RequestParam(defaultValue = "1") int page)
  {
    System.out.println("hit vr");
    return this.propertyService.getPropertyHavingVirtualTour(page);
  }

  @PatchMapping("/virtualTourURL/{id}")
  public Property setUrl(@PathVariable Integer id,@RequestBody String virtualTourURL,
                         @RequestHeader(name = "Authorization", defaultValue = "") String token)
  {
    System.out.println("hit virtual");
    return this.propertyService.setVirtualTourUrl(virtualTourURL,id,token);
  }

  @PatchMapping("/virtualTour/{id}")
  public Property setUrl(@PathVariable Integer id)
  {
    return this.propertyService.setVirtualTour(id);
  }

}
