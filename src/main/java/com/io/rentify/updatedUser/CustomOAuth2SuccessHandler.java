package com.io.rentify.updatedUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final MyUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2SuccessHandler.class);

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    public CustomOAuth2SuccessHandler(MyUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String email = token.getPrincipal().getAttribute("email");
        logger.info("Google login successful for email: " + email);


        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                token.getAuthorizedClientRegistrationId(), token.getName());
        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        logger.info("Bearer token: " + accessToken); // Log the actual Bearer token.


        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // Ensure authorities are loaded correctly for OAuth2 user
            Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(token.getPrincipal().getAttribute("name"));
            newUser.setRole("USER"); // Assign a default role for OAuth2 users
            newUser.setPassword(passwordEncoder.encode("google_oauth_user"));
            userRepository.save(newUser);

            // Set authentication for new user
            Authentication auth = new UsernamePasswordAuthenticationToken(newUser, null, newUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);


        }
        response.sendRedirect("/home");
    }
}
