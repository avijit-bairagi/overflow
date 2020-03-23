package com.mrrobot.overflow.profile.service;

import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.profile.entity.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ProfileService {

    Profile save(Profile profile);

    Profile update(Profile profile) throws NotFoundException;

    Optional<Profile> findByUserId(long userId);

    List<Profile> findAll(Pageable pageable);

    List<Profile> findAllBySorting(Sort point);
}
