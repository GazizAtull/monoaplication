package com.project.Lanch.service;

//adapter


/**
 * CustomUserDetailsService is an adapter that integrates the application's user model with Spring Security.
 * - Implements the UserDetailsService interface to provide user authentication.
 * - Functions:
 *   - `loadUserByUsername`: Retrieves a User from the database by username.
 *   - Converts the application's User roles into Spring Security GrantedAuthority objects.
 *   - Returns a Spring Security User object for authentication and authorization.
 * - Throws `UsernameNotFoundException` if the user is not found.
 */



import com.project.Lanch.model.User;
import com.project.Lanch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}
