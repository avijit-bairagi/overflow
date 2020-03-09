package com.mrrobot.overflow.profile.service;

import com.mrrobot.overflow.profile.entity.Profile;
import com.mrrobot.overflow.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService{

    @Autowired
    ProfileRepository profileRepository;

    @Override
    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    @Override
    public Optional<Profile> findByUserId(long userId) {
        return profileRepository.findByUserId(userId);
    }
}
