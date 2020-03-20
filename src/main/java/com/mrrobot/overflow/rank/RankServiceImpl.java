package com.mrrobot.overflow.rank;

import com.mrrobot.overflow.common.utils.UserLevel;
import com.mrrobot.overflow.profile.entity.Profile;
import com.mrrobot.overflow.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Configuration
@Service
public class RankServiceImpl implements RankService {

    @Value("${post.rank.defaultPoint}")
    private Double defaultPoint;

    @Autowired
    ProfileService profileService;

    @Override
    public Double getPoint(Long userId, boolean isUpVote) {

        Optional<Profile> profileOptional = profileService.findByUserId(userId);

        if (profileOptional.isEmpty())
            return defaultPoint;

        if(isUpVote)
            return UserLevel.findByLevel(profileOptional.get().getLevel()).getPositiveValue() * defaultPoint;
        else
            return -UserLevel.findByLevel(profileOptional.get().getLevel()).getNegativeValue() * defaultPoint;
    }
}
