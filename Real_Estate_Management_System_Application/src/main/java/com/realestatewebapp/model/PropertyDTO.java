package com.realestatewebapp.model;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class PropertyDTO {
	private Long id;
	private String sellingType;
	private String propertyType;
	private String flatType;
	private String city;
	private String locality;
	private String address;
	private int bedrooms;
	private int balconies;
	private double carpetArea;
	private int totalFloors;
	private String possession;
	private int propertyFloor;
	private double expectedPrice;
	private double pricePerSqFt;
	private String status;
	private List<PropertyImageDTO> images;
	private Long userId; 
	

	public PropertyDTO(Property property, List<PropertyImageDTO> images) {
		super();
		this.id = property.getId();
		this.sellingType = property.getSellingType();
		this.propertyType = property.getPropertyType();
		this.flatType = property.getFlatType();
		this.city = property.getCity();
		this.locality = property.getLocality();
		this.address = property.getAddress();
		this.bedrooms = property.getBedrooms();
		this.balconies = property.getBalconies();
		this.totalFloors= property.getTotalFloors(); 
		this.possession = property.getPossession();
		this.propertyFloor = property.getPropertyFloor();
		this.carpetArea = property.getCarpetArea();
		this.expectedPrice = property.getExpectedPrice();
		this.pricePerSqFt = property.getPricePerSqFt();
		this.status = property.getStatus();
		this.images = images;
		if (property.getUser() != null) {
	        this.userId = property.getUser().getId();
	    } else {
	        this.userId = null;  // Or handle as appropriate
	    }
	}
	


	public int getBedrooms() {
		return bedrooms;
	}

	public void setBedrooms(int bedrooms) {
		this.bedrooms = bedrooms;
	}

	public int getTotalFloors() {
		return totalFloors;
	}

	public void setTotalFloors(int totalFloors) {
		this.totalFloors = totalFloors;
	}

	public int getPropertyFloor() {
		return propertyFloor;
	}

	public void setPropertyFloor(int propertyFloor) {
		this.propertyFloor = propertyFloor;
	}
	public double getPricePerSqFt() {
		return pricePerSqFt;
	}

	public void setPricePerSqFt(double pricePerSqFt) {
		this.pricePerSqFt = pricePerSqFt;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSellingType() {
		return sellingType;
	}
	public void setSellingType(String sellingType) {
		this.sellingType = sellingType;
	}
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public String getFlatType() {
		return flatType;
	}
	public void setFlatType(String flatType) {
		this.flatType = flatType;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public int getBalconies() {
		return balconies;
	}
	public void setBalconies(int balconies) {
		this.balconies = balconies;
	}
	public String getPossession() {
		return possession;
	}
	public void setPossession(String possession) {
		this.possession = possession;
	}
	public double getCarpetArea() {
		return carpetArea;
	}
	public void setCarpetArea(double carpetArea) {
		this.carpetArea = carpetArea;
	}
	public double getExpectedPrice() {
		return expectedPrice;
	}
	public void setExpectedPrice(double expectedPrice) {
		this.expectedPrice = expectedPrice;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<PropertyImageDTO> getImages() {
		return images;
	}
	public void setImages(List<PropertyImageDTO> images) {
		this.images = images;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	

}
