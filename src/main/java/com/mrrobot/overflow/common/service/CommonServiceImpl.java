package com.mrrobot.overflow.common.service;

import com.mrrobot.overflow.common.entity.Config;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.exception.UserLoginException;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.profile.entity.User;
import com.mrrobot.overflow.profile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    ConfigService configService;

    @Autowired
    UserService userService;

    @Override
    public User checkUser(String username) throws NotFoundException, UserLoginException {

        Optional<User> userOptional = userService.findByUsername(username);

        if (userOptional.isEmpty())
            throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "User not found!");

        Config config = configService.getConfig();

        if (config.isApprovalNeeded()){

            if (!userOptional.get().isApproved())
                throw new UserLoginException(ResponseStatus.USER_NOT_APPROVED.value(), "User is not approved yet!");
        }

        return userOptional.get();
    }
}
