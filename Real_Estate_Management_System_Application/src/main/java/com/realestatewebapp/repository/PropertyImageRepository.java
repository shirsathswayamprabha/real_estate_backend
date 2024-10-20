package com.realestatewebapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realestatewebapp.model.PropertyImage;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {
	
	List<PropertyImage> findByPropertyId(Long propertyId);

}
