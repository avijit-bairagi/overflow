package com.mrrobot.overflow.profile.service;

import com.mrrobot.overflow.profile.model.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);
}
