package com.io.rentify.config;



import com.io.rentify.auth.JwtAuthenticationFilter;
import com.io.rentify.updatedUser.CustomOAuth2SuccessHandler;
import com.io.rentify.updatedUser.MyUserDetailService;
import com.io.rentify.updatedUser.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SpringConfig {

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private MyUserRepository userRepository;




    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {

                    registry.requestMatchers("/home", "/register/**", "/authenticate", "/password/forgot", "/password/reset", "/ws/**", "/login").permitAll();
                    registry.requestMatchers("/admin/**").hasRole("ADMIN");
                    registry.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN"); // Allow users with ROLE_USER
                    registry.requestMatchers("/ads/**").hasAnyRole("USER", "ADMIN");
                    registry.requestMatchers("/reviews/**").hasAnyRole("USER", "ADMIN");
                    registry.requestMatchers("/bookings/**").hasAnyRole("USER", "ADMIN");
                    registry.requestMatchers("/api/users/{id}").hasAnyRole("USER", "ADMIN");
                    registry.requestMatchers("/api/users/**").hasRole("ADMIN");


                    registry.anyRequest().authenticated();

                })
//                .exceptionHandling(handling -> handling
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                            response.getWriter().write("Unauthorized - Please login");
//                        })
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            response.setStatus(HttpStatus.FORBIDDEN.value());
//                            response.getWriter().write("Access Denied");
//                        })
//
//                )
                .oauth2Login(oauth2login -> {
                    oauth2login.successHandler(oAuth2SuccessHandler());
                })
                .formLogin(Customizer.withDefaults())
                .addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationSuccessHandler oAuth2SuccessHandler() {
        return new CustomOAuth2SuccessHandler(userRepository, passwordEncoder());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
