package com.mrrobot.overflow.rank;

import com.mrrobot.overflow.common.utils.UserLevel;
import com.mrrobot.overflow.profile.entity.Profile;
import com.mrrobot.overflow.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RankServiceImpl implements RankService {

    private final Double defaultPoint = 5.0;

    @Autowired
    ProfileService profileService;

    @Override
    public Double getPoint(Long userId, boolean isUpVote) {

        Double point = 0.0;

        Optional<Profile> profileOptional = profileService.findByUserId(userId);

        if (profileOptional.isEmpty())
            point = defaultPoint;
        else
            point = (double) UserLevel.findByLevel(profileOptional.get().getLevel()).getLevel() * defaultPoint;

        if(isUpVote)
            return point;
        else
            return -(point/2);
    }
}
