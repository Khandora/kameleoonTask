package com.kameleoon.test.service;

import com.kameleoon.test.model.entity.User;
import com.kameleoon.test.repository.UserRepository;
import com.kameleoon.test.security.jwt.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserProvider {
    private final UserRepository userRepository;

    public AuthenticatedUserProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtUser) {
            JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
            return userRepository.findById(jwtUser.getId()).get();
        } else {
            throw new RuntimeException("User is not authenticated!");
        }
    }
}
