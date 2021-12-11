package com.hashedin.virtualproperty.application.repository;

import com.hashedin.virtualproperty.application.entities.PropertyImage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PropertyImageRepository extends PagingAndSortingRepository<PropertyImage, String> {
  @Query(value = "select p.url as url, p.publicId as publicId from PropertyImage p where p.property.propertyId = ?1")
  List<Map> findAllByPropertyId(Integer propertyId);

  @Query(value = "select p.url from PropertyImage p where p.property.propertyId = ?1")
  List<String> findFirstImageById(Integer propertyId, Pageable pageable);

}
