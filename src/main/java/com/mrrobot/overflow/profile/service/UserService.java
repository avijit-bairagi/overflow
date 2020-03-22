package com.mrrobot.overflow.profile.service;

import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.profile.entity.User;
import com.mrrobot.overflow.security.model.UserData;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    List<User> findAll();

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User save(User user);

    User update(User user) throws NotFoundException;

    UserData getUserData();
}
