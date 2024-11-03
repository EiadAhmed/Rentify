package com.io.rentify.booking;

import com.io.rentify.updatedUser.MyUserRepository;
import com.io.rentify.updatedUser.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private MyUserRepository myUserRepository;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getUserFromDetails(userDetails);
        booking.setRenterId(user.getId());
        try {
            Booking createdBooking = bookingService.createBooking(booking);
            return ResponseEntity.ok(createdBooking);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Booking time overlaps with an existing booking for this ad.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/ad/{adId}")
    public ResponseEntity<List<Booking>> getBookingsForAd(@PathVariable Long adId) {
        List<Booking> bookings = bookingService.getBookingsForAd(adId);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody Booking booking, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getUserFromDetails(userDetails);
        booking.setRenterId(user.getId());
        if (!bookingService.getBookingById(id).getRenterId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Booking updatedBooking = bookingService.updateBooking(id, booking);
            return ResponseEntity.ok(updatedBooking);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Booking time overlaps with an existing booking for this ad.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        if (booking.getStatus().equals(BookingStatus.CONFIRMED)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Cannot delete a confirmed booking");
        }
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    private User getUserFromDetails(UserDetails userDetails) {
        return myUserRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}