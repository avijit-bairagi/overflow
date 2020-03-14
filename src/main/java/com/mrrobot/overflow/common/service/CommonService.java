package com.mrrobot.overflow.common.service;

import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.exception.UserLoginException;

public interface CommonService {

    void checkUser(String username) throws NotFoundException, UserLoginException;
}
