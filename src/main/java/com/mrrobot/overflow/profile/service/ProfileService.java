package com.mrrobot.overflow.profile.service;

import com.mrrobot.overflow.profile.model.Profile;

import java.util.Optional;

public interface ProfileService {

    Profile save(Profile profile);

    Optional<Profile> findByUserId(long userId);
}
