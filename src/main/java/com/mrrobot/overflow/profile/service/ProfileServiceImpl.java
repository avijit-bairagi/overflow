package com.mrrobot.overflow.profile.service;

import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.common.utils.UserLevel;
import com.mrrobot.overflow.profile.entity.Profile;
import com.mrrobot.overflow.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    ProfileRepository profileRepository;

    @Override
    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    @Override
    public Profile update(Profile profile) throws NotFoundException {

        if (profileRepository.findById(profile.getId()).isEmpty()) {

            throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "Profile not found!");
        }
        profile.setLevel(UserLevel.getLevelByPoint(profile.getPoint()));

        return profileRepository.save(profile);
    }

    @Override
    public Optional<Profile> findByUserId(long userId) {
        return profileRepository.findByUserId(userId);
    }
}
