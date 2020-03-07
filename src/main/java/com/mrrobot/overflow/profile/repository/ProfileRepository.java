package com.mrrobot.overflow.profile.repository;

import com.mrrobot.overflow.profile.model.Profile;
import com.mrrobot.overflow.profile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserId(long userId);
}
