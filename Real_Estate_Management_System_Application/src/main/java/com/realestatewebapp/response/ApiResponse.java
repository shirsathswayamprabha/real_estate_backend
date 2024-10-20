package com.realestatewebapp.response;

import java.util.Map;

public class ApiResponse {
	
	private String message;
	private Map<String, Object> data;
	
	public ApiResponse(String message, Map<String, Object> data) {
		super();
		this.message = message;
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	

}
