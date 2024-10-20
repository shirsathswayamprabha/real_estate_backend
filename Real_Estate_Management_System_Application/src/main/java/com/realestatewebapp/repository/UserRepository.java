package com.realestatewebapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realestatewebapp.model.AgentDTO;
import com.realestatewebapp.model.BuyerDTO;
import com.realestatewebapp.model.Property;
import com.realestatewebapp.model.Role;
import com.realestatewebapp.model.SellerDTO;
import com.realestatewebapp.model.User;
import com.realestatewebapp.model.UserDTO;

//@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
	Optional<User> findByResetToken(String resetToken);
	
	@Query("SELECT new com.realestatewebapp.model.AgentDTO(u.id,u.username,u.gender,u.email,u.mobileNumber, u.role)"
			+ "FROM User as u WHERE u.role ='AGENT'")
	List<AgentDTO> findAgents();
	
	@Query("SELECT new com.realestatewebapp.model.BuyerDTO(u.username,u.gender,u.email,u.mobileNumber, u.role)"
			+ "FROM User as u WHERE u.role ='BUYER'")
	List<BuyerDTO> findBuyers();
	
	@Query("SELECT new com.realestatewebapp.model.SellerDTO(u.username,u.gender,u.email,u.mobileNumber, u.role)"
			+ "FROM User as u WHERE u.role ='SELLER'")
	List<SellerDTO> findSellers();
	
	@Query("SELECT new com.realestatewebapp.model.UserDTO(u.id,u.username,u.gender,u.email,u.mobileNumber, u.role)"
			+ "FROM User as u WHERE u.id = :userId")
	List<UserDTO> findUsers(@Param("userId") Long userId);
	
    Optional<User> findById(Long id);


}
