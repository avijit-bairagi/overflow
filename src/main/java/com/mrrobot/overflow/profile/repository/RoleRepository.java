package com.mrrobot.overflow.profile.repository;

import java.util.Optional;

import com.mrrobot.overflow.profile.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}