package com.io.rentify.updatedUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class MyUserDetailService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailService.class);

    @Autowired
    private MyUserRepository repository;

    

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = repository.findByEmail(email);
        if (user.isPresent()) {
            var userObj = user.get();
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (String role : getRoles(userObj)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.trim()));
                logger.info("User role added: ROLE_" + role.trim());

            }
            return new CustomUserDetails(userObj, authorities);
        } else {
            throw new UsernameNotFoundException("User not found: " + email);
        }
    }

    private String[] getRoles(User user) {
        if (user.getRole() == null || user.getRole().isEmpty()) {
            return new String[]{"USER"}; // Default role if none provided
        }
        return user.getRole().split(","); // Split roles by comma
    }

    public List<User> findConnectedUsers() {
        return repository.findAllByStatus(Status.ONLINE);
    }
}
