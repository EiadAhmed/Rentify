package com.io.rentify.booking;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Data

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking")
public class Booking {

    @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
    private Long adId;
    private Long renterId;
    private LocalDateTime  startDate;
    private LocalDateTime bookingDate=LocalDateTime.now();
    private LocalDateTime endDate;
    private BookingStatus  status;

}
