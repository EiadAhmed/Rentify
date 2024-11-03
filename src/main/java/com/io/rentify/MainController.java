package com.io.rentify;



import com.io.rentify.updatedUser.MyUserDetailService;
import com.io.rentify.updatedUser.MyUserRepository;
import com.io.rentify.updatedUser.User;
import com.io.rentify.webtoken.JwtService;
import com.io.rentify.webtoken.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class MainController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private MyUserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;



    @GetMapping("/home")
    public String home() {
        return "welcome!";
    }
    @GetMapping("/admin/home")
    public String handelAdminHome() {
        return "home_admin!";
    }
    @GetMapping("/user/home")
    public String handelUserHome() {
        return "home_user!";
    }

    @GetMapping("/user")
    public Principal user(Principal user) {
        return user;

    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginForm.username(), loginForm.password()
        ));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(myUserDetailService.loadUserByUsername(loginForm.username()));
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> findConnectedUsers() {
        return ResponseEntity.ok(myUserDetailService.findConnectedUsers());
    }

    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        Optional<User> userOptional = userRepository.findByResetPasswordToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getTokenExpirationTime().isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetPasswordToken(null); // Clear token after successful reset
            user.setTokenExpirationTime(null);
            userRepository.save(user);

            return ResponseEntity.ok("Password reset successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }



}
