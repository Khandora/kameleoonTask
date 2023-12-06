package com.kameleoon.test.security;

import com.kameleoon.test.model.entity.User;
import com.kameleoon.test.security.jwt.JwtUser;
import com.kameleoon.test.security.jwt.JwtUserFactory;
import com.kameleoon.test.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userService.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        JwtUser jwtUser = JwtUserFactory.create(user.get());
        log.info("IN loadUserByUsername - user with username: {} successfully loaded", username);
        return jwtUser;
    }
}
