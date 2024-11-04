package com.io.rentify.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // Check if there are overlapping bookings for the same ad
    public boolean hasOverlap(Long adId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(adId, startDate, endDate);
        return !overlappingBookings.isEmpty();
    }

    // Create a new booking
    public Booking createBooking(Booking booking) {
        if (hasOverlap(booking.getAdId(), booking.getStartDate(), booking.getEndDate())) {
            throw new IllegalArgumentException("Booking time overlaps with an existing booking for this ad.");
        }
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        if (booking.getRenterId() == null) {
            throw new IllegalArgumentException("Renter ID cannot be null");
        }


        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);  // Default status

        return bookingRepository.save(booking);
    }

    // Retrieve a booking by ID
    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));
    }

    // Retrieve all bookings for a specific ad
    public List<Booking> getBookingsForAd(Long adId) {
        return bookingRepository.findAll()
                .stream()
                .filter(booking -> booking.getAdId().equals(adId))
                .collect(Collectors.toList());
    }

    // Update a booking
    public Booking updateBooking(Long bookingId, Booking updatedBooking) {
        Booking existingBooking = getBookingById(bookingId);

        if (hasOverlap(updatedBooking.getAdId(), updatedBooking.getStartDate(), updatedBooking.getEndDate())) {
            throw new IllegalArgumentException("Booking time overlaps with an existing booking for this ad.");
        }
        if (updatedBooking.getStartDate().isAfter(updatedBooking.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        if (!existingBooking.getRenterId().equals(updatedBooking.getRenterId())) {
            throw new IllegalArgumentException("Unauthorized to update this booking");
        }
        existingBooking.setStartDate(updatedBooking.getStartDate());
        existingBooking.setEndDate(updatedBooking.getEndDate());
        existingBooking.setStatus(updatedBooking.getStatus());
        existingBooking.setRenterId(updatedBooking.getRenterId());

        return bookingRepository.save(existingBooking);
    }

    // Delete a booking by ID
    public void deleteBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    // Retrieve all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
