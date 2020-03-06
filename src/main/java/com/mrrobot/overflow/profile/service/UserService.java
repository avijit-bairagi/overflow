package com.mrrobot.overflow.profile.service;

import com.mrrobot.overflow.profile.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User save(User user);
}
