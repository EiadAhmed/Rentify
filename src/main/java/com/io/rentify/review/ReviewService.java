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

        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (reviewRepository.findByAdIdAndUserId(review.getAdId(), review.getUserId()) != null) {
            throw new IllegalArgumentException("Only one review per ad is allowed");
        }
        return reviewRepository.save(review);
    }

    // Get a review by its ID
    public Optional<Review> getReviewById(Long reviewId) {
        if (reviewId == null) {
            throw new IllegalArgumentException("Review ID cannot be null");
        }

        return reviewRepository.findById(reviewId);
    }

    // Get all reviews for a specific ad
    public List<Review> getReviewsByAdId(Long adId) {
        return reviewRepository.findByAdId(adId);
    }

    // Update an existing review
    public Review updateReview(Review updatedReview)
    {
        if (updatedReview.getRating() < 1 || updatedReview.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (reviewRepository.findByAdIdAndUserId(updatedReview.getAdId(), updatedReview.getUserId()) == null) {
            throw new IllegalArgumentException("Review does not exist");
        }
        Review existingReview =
                reviewRepository.findByAdIdAndUserId(updatedReview.getAdId(), updatedReview.getUserId());
        if (!existingReview.getUserId().equals(updatedReview.getUserId())) {
            throw new IllegalArgumentException("User ID does not match");
        }
        existingReview.setReview_text(updatedReview.getReview_text());
        existingReview.setRating(updatedReview.getRating());


        return reviewRepository.save(existingReview);
    }

    // Delete a review by its ID
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}