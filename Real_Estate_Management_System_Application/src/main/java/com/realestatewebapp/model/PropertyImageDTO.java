package com.realestatewebapp.model;

import java.util.Base64;

public class PropertyImageDTO {

	private Long id;
	private String fileName;
	private String imageBase64;


	public PropertyImageDTO(PropertyImage propertyImage) {
		this.id = propertyImage.getId();
		this.fileName = propertyImage.getFileName();
//		this.imageBase64 = Base64.getEncoder().encodeToString(propertyImage.getImageData());
		  if (propertyImage.getImageData() != null) {
	            this.imageBase64 = Base64.getEncoder().encodeToString(propertyImage.getImageData());
	        } else {
	            this.imageBase64 = null;
	        }
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getImageBase64() {
		return imageBase64;
	}

	public void setImageBase64(String imageBase64) {
		this.imageBase64 = imageBase64;
	}


}
