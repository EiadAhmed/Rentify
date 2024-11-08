package com.io.rentify.updatedUser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    // Expose additional fields

    public Long getId() {
        return user.getId();
    }

    public String getPhone() {
        return user.getPhone();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public boolean isPremiumStatus() {
        return user.isPremiumStatus();
    }

    public float getRatings() {
        return user.getRatings();
    }

    public String[] getRoles() {
        return user.getRole() != null ? user.getRole().split(",") : new String[]{};
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("Authorities: " + authorities);
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Consider returning email as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
