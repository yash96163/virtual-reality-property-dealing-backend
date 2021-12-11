package com.hashedin.virtualproperty.application.service;

import com.hashedin.virtualproperty.application.entities.Property;
import com.hashedin.virtualproperty.application.entities.PropertyImage;
import com.hashedin.virtualproperty.application.entities.User;
import com.hashedin.virtualproperty.application.exceptions.CustomException;
import com.hashedin.virtualproperty.application.exceptions.UnauthorizedException;
import com.hashedin.virtualproperty.application.repository.PropertyImageRepository;
import com.hashedin.virtualproperty.application.repository.PropertyRepo;
import com.hashedin.virtualproperty.application.request.PropertyRequest;
import com.hashedin.virtualproperty.application.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepo propertyRepo;

    @Autowired
    private AuthService authService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PropertyImageRepository propertyImageRepository;

    private final Logger logger = LoggerFactory.getLogger(PropertyService.class);

    public PropertyFull createProperty(PropertyRequest propertyRequest, String token) {
        Property property = this.convertPropertyRequestToProperty(propertyRequest);
        User user = authService.getUserFromToken(token);
        property.setOwnerEmail(user.getEmail());
        Property savedProperty = propertyRepo.save(property);
        return this.convertPropertyToPropertyFull(savedProperty);
    }

    public PropertyResponse getProperty(
            int minPrice,
            int maxPrice,
            String street,
            String city,
            String state,
            String type,
            String purpose,
            int page) {
        this.logger.info(
                "GET PROPERTY: PRICE:" + minPrice + "-" + maxPrice + ", STREET: " + street
                        + " CITY: " + city + ", STATE: " + state + ", TYPE: " + type + ", PURPOSE: " + purpose + ", PAGE:" + page
        );
        if (page < 1) {
            throw new CustomException("Page number should be greater than 0");
        }
        // page starts at 0, displaying 0 to user will confuse them, so we give them option from 1-page
        // and subtract 1
        Pageable pageNumber = PageRequest.of(page - 1, 10);
        Page<Property> propertyList =
                propertyRepo.getProperties(
                        minPrice, maxPrice, street, city, state, type, purpose, pageNumber);
        this.logger.info("TOTAL RECORDS: " + propertyList.getTotalElements());
        if (propertyList.isEmpty()) throw new CustomException("No Property Found....");
        ArrayList<PropertyShort> propertyData = new ArrayList<>();
        for (Property property : propertyList) {
            propertyData.add(this.convertPropertyToPropertyShort(property));
        }
        return new PropertyResponse(
                page, propertyList.getTotalPages(), propertyList.getTotalElements(), propertyData);
    }

    public PropertyFull getPropertyById(Integer propertyId) {
        this.logger.info("FETCHING PROPERTY WITH ID: " + propertyId);
        Property property = propertyRepo.getPropertiesById(propertyId);

        if (property == null) throw new CustomException("No Property Found with id " + propertyId);
        return this.convertPropertyToPropertyFull(property);
    }

    public PropertyResponse getOwnerProperty(String email, int page) {
        this.logger.info("GETTING PROPERTY OF OWNER WITH EMAIL: " + email);
        if (page < 1) {
            throw new CustomException("Page number should be greater than 0");
        }
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Property> property = propertyRepo.getOwnerProperty(email, pageable);

        if (property == null) throw new CustomException("No Property Found ");
        ArrayList<PropertyShort> propertyData = new ArrayList<>();
        for (Property value : property) {
            propertyData.add(this.convertPropertyToPropertyShort(value));
        }
        this.logger.info("PROPERTIES FOUND: " + property.getTotalElements());
        return new PropertyResponse(
                page, property.getTotalPages(), property.getTotalElements(), propertyData);
    }

    public Property deleteProperty(Integer propertyId, String token) {
        User user = this.authService.getUserFromToken(token);
        String userEmail = user.getEmail();
        this.logger.info("DELETING PROPERTY " + propertyId + " BY USER " + userEmail);
        Optional<Property> propertyOptional = propertyRepo.findById(propertyId);
        if (propertyOptional.isEmpty()) {
            throw new CustomException("Property not found");
        }
        Property property = propertyOptional.get();
        if (!property.getOwnerEmail().equalsIgnoreCase(userEmail)) {
            this.logger.warn("USER WITH EMAIL " + userEmail + " PERFORMED UNAUTHORIZED ACTION");
            throw new UnauthorizedException("Unauthorized to delete property created by others");
        }
        propertyRepo.delete(property);
        // return the deleted property
        return property;
    }

    public PropertyFull editProperty(PropertyRequest property, Integer id, String token) {
        User user = this.authService.getUserFromToken(token);
        String userEmail = user.getEmail();
        this.logger.info("EDITING PROPERTY " + id + " BY USER " + userEmail);
        Property property1 = propertyRepo.getPropertiesById(id);
        if (!property1.getOwnerEmail().equalsIgnoreCase(userEmail)) {
            // cannot edit property of others
            this.logger.warn("USER WITH EMAIL " + userEmail + " PERFORMED UNAUTHORIZED ACTION");
            throw new UnauthorizedException("Unauthorized to perform this action");
        }
        property1.setAddress(property.getAddress());
        property1.setArea(property.getArea());
        property1.setBathrooms(property.getBathrooms());
        property1.setBedrooms(property.getBedrooms());
        property1.setBhk(property.getBhk());
        property1.setBuiltYear(property.getBuiltYear());
        property1.setCity(property.getCity());
        property1.setFloors(property.getFloors());
        property1.setState(property1.getState());
        property1.setDescription(property.getDescription());
        property1.setPurpose(property.getPurpose());
        property1.setType(property.getType());
        property1.setPrice(property.getPrice());
        property1.setPinCode(property.getPinCode());
        Property savedProperty = propertyRepo.save(property1);
        return this.convertPropertyToPropertyFull(savedProperty);
    }

    public PropertyFull addImage(Integer propertyId, MultipartFile image, String token)
            throws IOException {
        User user = this.authService.getUserFromToken(token);
        String userEmail = user.getEmail();
        this.logger.info("ADDING IMAGE TO PROPERTY " + propertyId + " BY USER " + userEmail);
        Optional<Property> propertyContainer = this.propertyRepo.findById(propertyId);
        if (propertyContainer.isEmpty()) {
            throw new CustomException("Invalid Property ID");
        }
        Property property = propertyContainer.get();
        if (!property.getOwnerEmail().equalsIgnoreCase(userEmail)) {
            this.logger.warn("USER WITH EMAIL " + userEmail + " PERFORMED UNAUTHORIZED ACTION");
            throw new UnauthorizedException("Unauthorized to perform this action");
        }
        FileResponse response = this.fileStorageService.storeFile(image);
        PropertyImage propertyImage = new PropertyImage(response.id, response.url, response.name, property);
        this.propertyImageRepository.save(propertyImage);
        return this.convertPropertyToPropertyFull(property);
    }

    public PropertyFull deleteImage(String imageId, String token) throws Exception {
        User user = this.authService.getUserFromToken(token);
        String userEmail = user.getEmail();
        this.logger.info("DELETING IMAGE WITH ID " + imageId + " BY USER " + userEmail);
        Optional<PropertyImage> propertyImageOptional = this.propertyImageRepository.findById(imageId);
        if (propertyImageOptional.isEmpty()) {
            throw new CustomException("Image with id " + imageId + " not found");
        }
        PropertyImage image = propertyImageOptional.get();
        Property property = image.getProperty();
        if (!property.getOwnerEmail().equalsIgnoreCase(userEmail)) {
            this.logger.warn("USER WITH EMAIL " + userEmail + " PERFORMED UNAUTHORIZED ACTION");
            throw new CustomException("Unauthorized to delete other user's images");
        }
        FileResponse file = new FileResponse(image.getUrl(), image.getPublicId(), image.getName());
        this.fileStorageService.deleteFile(file);
        this.propertyImageRepository.delete(image);
        return this.convertPropertyToPropertyFull(property);
    }

    private PropertyShort convertPropertyToPropertyShort(Property property) {
        Pageable p = PageRequest.of(0, 1);
        List<String> thumbImageOptional =
                this.propertyImageRepository.findFirstImageById(property.getPropertyId(), p);
        String thumbImage = "";
        if (thumbImageOptional.size() > 0) {
            thumbImage = thumbImageOptional.get(0);
        }
        System.out.println(property.isVirtualTour());
        return new PropertyShort(
                property.getPropertyId(),
                property.getCity(),
                property.getState(),
                property.getAddress(),
                property.getArea(),
                property.getBhk(),
                property.getPrice(),
                property.getFloors(),
                property.getBedrooms(),
                property.getBathrooms(),
                property.getPinCode(),
                thumbImage, property.getPurpose(), property.getBuiltYear(), property.getType(),property.getOwnerEmail(),property.isVirtualTour(),property.getVirtualTourURL());
    }

    private PropertyFull convertPropertyToPropertyFull(Property property) {
        List<Map> imagesFromDb =
                this.propertyImageRepository.findAllByPropertyId(property.getPropertyId());

        ArrayList<Image> images = new ArrayList<>();
        for (Map map : imagesFromDb) {
            images.add(new Image(map.get("publicId").toString(), map.get("url").toString()));
        }
        return new PropertyFull(
                property.getPropertyId(),
                property.getCity(),
                property.getState(),
                property.getAddress(),
                property.getType(),
                property.getPurpose(),
                property.getOwnerEmail(),
                property.getDescription(),
                property.getArea(),
                property.getBhk(),
                property.getBuiltYear(),
                property.getPrice(),
                property.getFloors(),
                property.getBedrooms(),
                property.getBathrooms(),
                property.getPinCode(),
                images,
                property.isVirtualTour(),
                property.getVirtualTourURL());
    }


    private Property convertPropertyRequestToProperty(PropertyRequest request) {
        return new Property(
                request.city,
                request.state,
                request.address,
                request.type,
                request.purpose,
                request.description,
                request.area,
                request.bhk,
                request.builtYear,
                request.price,
                request.floors,
                request.bedrooms,
                request.bathrooms,
                request.pinCode,
                "",
                request.virtualTour);
    }

    public PropertyResponse getPropertyHavingVirtualTour(int page)
    {
        if (page < 1) {
            throw new CustomException("Page number should be greater than 0");
        }

        // page starts at 0, displaying 0 to user will confuse them, so we give them option from 1-page
        // and subtract 1
        System.out.println(page);
        Pageable pageNumber = PageRequest.of(page - 1, 10);
        Page<Property> propertyList =propertyRepo.getPropertyHavingVirtualTour(true,pageNumber);
        System.out.println(propertyList);
        this.logger.info("TOTAL RECORDS: " + propertyList.getTotalElements());
        if (propertyList.isEmpty()) throw new CustomException("No Property Found....");
        ArrayList<PropertyShort> propertyData = new ArrayList<>();
        for (Property property : propertyList) {
            propertyData.add(this.convertPropertyToPropertyShort(property));
        }
        return new PropertyResponse(
                page, propertyList.getTotalPages(), propertyList.getTotalElements(), propertyData);
    }

    public Property setVirtualTourUrl(String url, Integer id, String token)
    {
        User user=this.authService.getUserFromToken(token);
        if(!user.isAdministrator())
        {
            this.logger.warn("USER WITH EMAIL " + user.getEmail() + " PERFORMED UNAUTHORIZED ACTION");
            throw new UnauthorizedException("Unauthorized to perform this action");
        }

        Property property=propertyRepo.getPropertiesById(id);
        if(property==null)
            throw new CustomException("No Property Found with id " + id);

        System.out.println(property);
        System.out.println(property.getVirtualTourURL()+" "+property.isVirtualTour());
        property.setVirtualTour(false);
        property.setVirtualTourURL(url);
        propertyRepo.save(property);
        System.out.println(property.getVirtualTourURL()+" "+property.isVirtualTour());
        System.out.println(property);
        return property;
    }

    public Property setVirtualTour(Integer id)
    {
        Property property=propertyRepo.getPropertiesById(id);
        property.setVirtualTour(true);
        propertyRepo.save(property);
        return property;
    }

}
