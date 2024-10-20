package com.realestatewebapp.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class MainController {
	@GetMapping("/welcome")
    public String getWelcomePage() {
        return "<h1>Welcome to Real Estate Management System<h1>";
    }

}
