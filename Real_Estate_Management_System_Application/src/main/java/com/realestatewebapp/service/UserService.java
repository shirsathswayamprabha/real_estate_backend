package com.realestatewebapp.service;

//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.realestatewebapp.model.User;
//import com.realestatewebapp.repository.UserRepository;
//
//@Service
//public class UserService implements UserDetailsService{
//	
//	private final UserRepository userRepository;
//	
//	@Autowired
//	public UserService(UserRepository userRepository) {
//		this.userRepository = userRepository;
//	}
//
//	@Override
//	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//		// TODO Auto-generated method stub
//		User user = userRepository.findByEmail(email)
//				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
//		return new org.springframework.security.core.userdetails.User(
//				user.getEmail(), 
//				user.getPassword(), 
//				List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole())));
//	}
//	
//	public User save(User user) {
//		return userRepository.save(user);
//	}
//
//}


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.realestatewebapp.exception.ResourceNotFoundException;
import com.realestatewebapp.model.AgentDTO;
import com.realestatewebapp.model.Role;
import com.realestatewebapp.model.User;
import com.realestatewebapp.model.UserDTO;
//import com.realestatewebapp.repository.UserDTORepository;
import com.realestatewebapp.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{


	private final UserRepository userRepository;

	//	@Autowired
	//	private UserDTORepository userDTORepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword(),
				List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
				);
	}


	public User save(User user) {
		return userRepository.save(user);

	}


	public User updateUserProfile(Long userId, String username, String gender,String mobileNumber) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		if(username != null && !username.isEmpty()) {
			user.setUsername(username);	
		}
		if(mobileNumber != null && !mobileNumber.isEmpty()) {
			user.setMobileNumber(mobileNumber);
		}
		if(gender != null && !gender.isEmpty()) {
			user.setGender(gender);
		}

		return userRepository.save(user);

	}


}

