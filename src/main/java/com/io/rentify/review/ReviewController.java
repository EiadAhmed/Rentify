package com.io.rentify.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewService.saveReview(review);
    }

    @GetMapping("/{id}")
    public Optional<Review> getReview(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping("/ad/{adId}")
    public List<Review> getReviewsByAdId(@PathVariable Long adId) {
        return reviewService.getReviewsByAdId(adId);
    }

    @PutMapping
    public Review updateReview(@RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
