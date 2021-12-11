package com.hashedin.virtualproperty.application.repository;

import com.hashedin.virtualproperty.application.entities.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepo extends PagingAndSortingRepository<Property, Integer> {

    @Query("select p from Property p where " +
            "p.price>=?1 and p.price<=?2 and " +
            "p.address like %?3 and p.city like %?4 and p.state like %?5 and " +
            "p.type like %?6 and p.purpose like %?7")
    Page<Property> getProperties(int minPrice, int maxPrice, String address, String city, String state,
                                 String type, String purpose, Pageable pageable);

    @Query("select p from Property p where p.ownerEmail=?1")
     Page<Property> getOwnerProperty(String email, Pageable pageable);

    @Query("select p from Property p where propertyId =?1")
     Property getPropertiesById(Integer id);

    @Query("select p from Property p where p.virtualTour = true")
    Page<Property> getPropertyHavingVirtualTour(boolean res,Pageable pageable);
}
