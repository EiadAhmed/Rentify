package com.io.rentify.review;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "review")
public class Review {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long reviewId;
    Long userId;
    Long adId;
    String review_text;
    float rating;
    String date= LocalDateTime.now().toString();

}
