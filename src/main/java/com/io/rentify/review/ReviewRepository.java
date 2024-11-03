package com.io.rentify.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByAdId(Long adId); // Find reviews for a specific ad
    Review findByAdIdAndUserId(Long adId, Long userId); // Find a review by ad and user ID
}