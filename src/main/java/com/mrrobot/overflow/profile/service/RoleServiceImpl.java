package com.mrrobot.overflow.profile.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.profile.entity.Role;
import com.mrrobot.overflow.profile.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role save(Role role) throws AlreadyExitsException {

        if (roleRepository.findByName(role.getName()).isPresent()) {

            throw new AlreadyExitsException(ResponseStatus.ALREADY_EXITS.value(), role.getName() + " already exits!");
        }

        return roleRepository.save(role);
    }

    @Override
    public Role update(Role role) throws NotFoundException {

        if (roleRepository.findById(role.getId()).isEmpty()) {

            throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "Role not found!");
        }

        return roleRepository.save(role);
    }
}
