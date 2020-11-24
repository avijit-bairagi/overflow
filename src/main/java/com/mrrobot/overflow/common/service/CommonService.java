package com.mrrobot.overflow.common.service;

import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.exception.UserLoginException;
import com.mrrobot.overflow.profile.entity.User;

public interface CommonService {

    User checkUser(String username) throws NotFoundException, UserLoginException;
}
