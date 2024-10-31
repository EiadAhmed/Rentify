package com.io.rentify.updatedUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    List<User> findAllByStatus(Status status);
    Optional<User> findByResetPasswordToken(String token); // New method for reset token

}
