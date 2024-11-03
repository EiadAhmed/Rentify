package com.io.rentify.updatedUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
public class PasswordController {

    @Autowired
    private MyUserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender; // Spring Boot mail sender

    @PostMapping("/password/forgot")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = UUID.randomUUID().toString(); // Generate reset token
            user.setResetPasswordToken(token);
            user.setTokenExpirationTime(LocalDateTime.now().plusHours(1)); // Token valid for 1 hour
            userRepository.save(user);

            String resetUrl = "http://yourdomain.com/password/reset?token=" + token;
//            sendResetEmail(user.getEmail(), resetUrl);
            System.out.println("Reset link: " + resetUrl);
            return ResponseEntity.ok("Password reset link sent to your email.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }
    }

    private void sendResetEmail(String email, String resetUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("Click the following link to reset your password: " + resetUrl);
        mailSender.send(message);
    }
}
