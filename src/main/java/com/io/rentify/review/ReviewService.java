package com.io.rentify.review;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ReviewService {


    @Autowired
    private ReviewRepository reviewRepository;

    // Save a new review
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    // Get a review by its ID
    public Optional<Review> getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    // Get all reviews for a specific ad
    public List<Review> getReviewsByAdId(Long adId) {
        return reviewRepository.findByAdId(adId);
    }

    // Update an existing review
    public Review updateReview(Review updatedReview) {
        return reviewRepository.save(updatedReview);
    }

    // Delete a review by its ID
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
