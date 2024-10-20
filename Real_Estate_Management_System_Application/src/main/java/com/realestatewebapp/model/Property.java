package com.realestatewebapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GenerationType;

@Entity

public class Property {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonProperty("sellingType")
	private String sellingType;// Rent, Sell

	@JsonProperty("propertyType")
	private String propertyType; // Residential
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
	private String status = "Not Approve";

	@OneToMany(mappedBy = "property", fetch = FetchType.LAZY)
	private List<PropertyImage> images;

	@ManyToOne
	@JoinColumn(name = "user_id") // This is the foreign key column in the Property table
	private User user;

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

	public int getBedrooms() {
		return bedrooms;
	}

	public void setBedrooms(int bedrooms) {
		this.bedrooms = bedrooms;
	}

	public int getBalconies() {
		return balconies;
	}

	public void setBalconies(int balconies) {
		this.balconies = balconies;
	}

	public double getCarpetArea() {
		return carpetArea;
	}

	public void setCarpetArea(double carpetArea) {
		this.carpetArea = carpetArea;
	}

	public int getTotalFloors() {
		return totalFloors;
	}

	public void setTotalFloors(int totalFloors) {
		this.totalFloors = totalFloors;
	}

	public String getPossession() {
		return possession;
	}

	public void setPossession(String possession) {
		this.possession = possession;
	}

	public int getPropertyFloor() {
		return propertyFloor;
	}

	public void setPropertyFloor(int propertyFloor) {
		this.propertyFloor = propertyFloor;
	}

	public double getExpectedPrice() {
		return expectedPrice;
	}

	public void setExpectedPrice(double expectedPrice) {
		this.expectedPrice = expectedPrice;
	}

	public double getPricePerSqFt() {
		return pricePerSqFt;
	}

	public void setPricePerSqFt(double pricePerSqFt) {
		this.pricePerSqFt = pricePerSqFt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<PropertyImage> getImages() {
		return images;
	}

	public void setImages(List<PropertyImage> images) {
		this.images = images;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	} 


}


