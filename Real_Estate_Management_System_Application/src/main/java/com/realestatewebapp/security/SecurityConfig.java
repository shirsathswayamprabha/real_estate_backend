package com.realestatewebapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	 private final UserDetailsService userDetailsService;

	    public SecurityConfig(UserDetailsService userDetailsService) {
	        this.userDetailsService = userDetailsService;
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .csrf().disable()  // Disable CSRF for simplicity (but enable in production)
	            .authorizeRequests()
	                .requestMatchers("/api/auth/**").permitAll()  // Public access for registration and login
	                .requestMatchers("/api/properties/**").permitAll()
	                .requestMatchers("/api/admin/**").hasRole("ADMIN")
	                .requestMatchers("/api/seller/**").hasRole("SELLER")
	                .requestMatchers("/api/buyer/**").hasRole("BUYER")
	                .requestMatchers("/api/buyer/**").hasRole("AGENT")
	                
	                .anyRequest().authenticated()
	            .and()
	            .formLogin().defaultSuccessUrl("/welcome", true)
	            .and()
	            .logout().permitAll();

	        return http.build();
	    }

//	    @Bean
//	    public PasswordEncoder passwordEncoder() {
//	        return new BCryptPasswordEncoder();
//	    }
	    
	    @Bean BCryptPasswordEncoder passwordEncoder() {
	    	 return new BCryptPasswordEncoder();
	    }

}

