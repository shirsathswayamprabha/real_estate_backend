package com.realestatewebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication
public class RealEstateManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealEstateManagementSystemApplication.class, args);
	}

}
