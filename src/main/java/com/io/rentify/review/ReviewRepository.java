package com.io.rentify.review;

import com.io.rentify.Ad.Ad;
import com.io.rentify.updatedUser.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByAdId(Long adId); // Find reviews for a specific ad
}