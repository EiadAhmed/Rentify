package com.io.rentify.ad;


import com.io.rentify.updatedUser.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findByUser(User user); // Retrieve ads by user
    List<Ad> findByAvailability(Ad.Availability availability);
}
