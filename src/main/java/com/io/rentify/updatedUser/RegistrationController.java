package com.io.rentify.updatedUser;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegistrationController {


    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register/user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (myUserRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email already exists"));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = myUserRepository.save(user);

        UserResponseDTO response = new UserResponseDTO(
                savedUser.getEmail(),
                savedUser.getName(),
                "User registered successfully!"
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
