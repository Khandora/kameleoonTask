package com.kameleoon.test.service;

import com.kameleoon.test.model.entity.Role;
import com.kameleoon.test.model.entity.User;
import com.kameleoon.test.model.enums.Status;
import com.kameleoon.test.repository.RoleRepository;
import com.kameleoon.test.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        Role roleUser = getRoleOrElseCreate();
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);

        User registeredUser = userRepository.save(user);

        log.info("IN register - user: {} successfully registered", registeredUser);

        return registeredUser;
    }

    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    public Optional<User> findByUsername(String username) {
        Optional<User> result = userRepository.findByUsername(username);
        log.info("IN findByUsername - user: {} found by username: {}", result, username);
        return result;
    }

    public Optional<User> findByEmail(String email) {
        Optional<User> result = userRepository.findByEmail(email);
        log.info("IN findByEmail - user: {} found by email: {}", result, email);
        return result;
    }

    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no user found by id: {}", id);
            return null;
        }

        log.info("IN findById - user: {} found by id: {}", result, id);
        return result;
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted", id);
    }

    private Role getRoleOrElseCreate() {
        Optional<Role> role = roleRepository.findByName("ROLE_USER");
        return role.orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
    }
}
