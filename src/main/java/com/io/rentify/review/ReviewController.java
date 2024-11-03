package com.io.rentify.review;

import com.io.rentify.updatedUser.MyUserRepository;
import com.io.rentify.updatedUser.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private MyUserRepository myUserRepository;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getUserFromDetails(userDetails);
        review.setUserId(user.getId());
        Review savedReview = reviewService.saveReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReview(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        return ResponseEntity.ok(review);
    }

    @GetMapping("/ad/{adId}")
    public ResponseEntity<List<Review>> getReviewsByAdId(@PathVariable Long adId) {
        List<Review> reviews = reviewService.getReviewsByAdId(adId);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review review, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getUserFromDetails(userDetails);
        review.setUserId(user.getId());
        Review existingReview = reviewService.getReviewById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        if (!Objects.equals(existingReview.getUserId(), user.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Review updatedReview = reviewService.updateReview(review);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getUserFromDetails(userDetails);
        Review existingReview = reviewService.getReviewById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        if (!Objects.equals(existingReview.getUserId(), user.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    private User getUserFromDetails(UserDetails userDetails) {
        return myUserRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}