//package com.io.rentify.auth;
//
//import com.io.rentify.updatedUser.CustomUserDetails;
//import com.io.rentify.updatedUser.MyUserRepository;
//import com.io.rentify.updatedUser.User;
//import com.io.rentify.webtoken.JwtService;
//import io.jsonwebtoken.io.IOException;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//
//public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final MyUserRepository userRepository;
//    private final JwtService jwtService;
//
//    public CustomOAuth2SuccessHandler(MyUserRepository userRepository, JwtService jwtService) {
//        this.userRepository = userRepository;
//        this.jwtService = jwtService;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException, ServletException, java.io.IOException {
//
//        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
//        String username = oauth2Token.getPrincipal().getName();
//
//        // Extract user details (from OAuth2)
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
//
//        // Generate JWT token
//        String token = jwtService.generateToken(new CustomUserDetails(user, user.getAuthorities()));
//
//        // Set the token as response
//        response.setHeader("Authorization", "Bearer " + token);
//
//        // Optional: Send the token in the body as well
//        response.getWriter().write("{\"token\": \"" + token + "\"}");
//        response.getWriter().flush();
//    }
//}
//
