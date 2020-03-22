package com.mrrobot.overflow.profile.service;

import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.profile.entity.Profile;

import java.util.Optional;

public interface ProfileService {

    Profile save(Profile profile);

    Profile update(Profile profile) throws NotFoundException;

    Optional<Profile> findByUserId(long userId);
}
