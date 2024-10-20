package com.realestatewebapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.realestatewebapp.model.Property;
import com.realestatewebapp.model.PropertyDTO;
import com.realestatewebapp.model.PropertyImageDTO;
import com.realestatewebapp.model.SellerDTO;
import com.realestatewebapp.model.User;

import jakarta.transaction.Transactional;

public interface PropertyRepository extends JpaRepository<Property, Long>{

	List<Property> findByUser(User user);
	 List<Property> findByStatus(String status);
	 List<Property> findByStatusAndUserId(String status, Long userId);

//	@Query("SELECT new com.realestatewebapp.model.PropertyDTO(p.id,p.sellingType,p.propertyType,"
//			+ "p.flatType,p.city,p.locality,p.address,p.bedrooms,p.balconies,p.carpetArea,p.totalFloors,"
//			+ "p.possession,p.propertyFloor,p.expectedPrice,p.pricePerSqFt,p.status,"
//			+ "pi.imageData,"
//			+ "p.user.id)"
//			+ "FROM Property as p "
//			+ "JOIN p.images pi "
//			+ "WHERE p.status ='Request'")
//	List<PropertyDTO> findPropertyDetails();

//	@Query("SELECT new com.realestatewebapp.model.PropertyDTO(p.id,p.sellingType,p.propertyType,"
//			+ "p.flatType,p.city,p.locality,p.address,p.bedrooms,p.balconies,p.carpetArea,p.totalFloors,"
//			+ "p.possession,p.propertyFloor,p.expectedPrice,p.pricePerSqFt,p.status,"
//			+ "pi.imageData,p.user.id)"
//			+ "FROM Property as p "
//			+ "JOIN p.images pi "
//			+ "WHERE p.status ='Approved'")
//	List<PropertyDTO> findApprovedPropertyDetails();
	
//	@Query("SELECT new com.realestatewebapp.model.PropertyDTO(p.id,p.sellingType,p.propertyType,"
//			+ "p.flatType,p.city,p.locality,p.address,p.bedrooms,p.balconies,p.carpetArea,p.totalFloors,"
//			+ "p.possession,p.propertyFloor,p.expectedPrice,p.pricePerSqFt,p.status,"
//			+ "pi.imageData,p.user.id)"
//			+ "FROM Property as p "
//			+ "JOIN p.images pi "
//			+ "WHERE p.status ='Buy'")
//	List<PropertyDTO> findBuiedPropertyDetails();
	
	@Modifying
	@Transactional
	@Query("UPDATE Property p SET p.status = :status WHERE p.user.id = :userId AND p.id = :propertyId")
	void updateStatus(@Param("userId") Long userId, @Param("propertyId") Long propertyId, @Param("status") String status);

	@Query("SELECT p FROM Property p WHERE p.user.id = :userId AND p.id = :propertyId")
	Optional<Property> findByUserIdAndPropertyId(@Param("userId") Long userId, @Param("propertyId") Long propertyId);

//	@Query("SELECT new com.realestatewebapp.model.PropertyDTO(p.id,p.sellingType,p.propertyType,"
//			+ "p.flatType,p.city,p.locality,p.address,p.bedrooms,p.balconies,p.carpetArea,p.totalFloors,"
//			+ "p.possession,p.propertyFloor,p.expectedPrice,p.pricePerSqFt,p.status,"
//			+ "pi.imageData,p.user.id)"
//			+ "FROM Property as p "
//			+ "JOIN p.images pi "
//			+ "WHERE p.status ='Bookmarked' AND p.user.id = :userId")
//	List<PropertyDTO> findBookmarkedPropertiesByUserIdAndStatus(@Param("userId") Long userId);

	@Query("SELECT p FROM Property p WHERE p.user.id = :userId")
	Property findByUserId(Long userId);
}
