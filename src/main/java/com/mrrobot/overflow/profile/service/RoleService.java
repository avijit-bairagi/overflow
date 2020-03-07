package com.mrrobot.overflow.profile.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.profile.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);
    List<Role> findAll();
    Role save(Role role) throws AlreadyExitsException;
    Role update(Role role) throws NotFoundException;
}
