package com.io.rentify.ad;


import com.io.rentify.updatedUser.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findByUser(User user); // Retrieve ads by user
    List<Ad> findByAvailability(Ad.Availability availability);
    List<Ad> findByTitleContainingIgnoreCase(String title);  // Search by title keyword
    List<Ad> findByLocationContainingIgnoreCase(String location); // Search by location
    List<Ad> findByCategoryContainingIgnoreCase(String category); // Filter by category
    List<Ad> findByPriceBetween(float minPrice, float maxPrice); // Filter by price range
    List<Ad> findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(String title, String location); // Search by both title and location
    List<Ad> findByCategoryContainingIgnoreCaseAndAvailability(String category, Ad.Availability availability); // Filter by category and availability
    List<Ad> findByApprovalStatus(Ad.ApprovalStatus approvalStatus);
    Page<Ad> findByApprovalStatus(Ad.ApprovalStatus approvalStatus, Pageable pageable);

}
