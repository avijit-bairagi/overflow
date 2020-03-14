package com.mrrobot.overflow.common.service;

import com.mrrobot.overflow.common.entity.Config;
import com.mrrobot.overflow.common.exception.NotFoundException;

public interface ConfigService {

    Config getConfig() throws NotFoundException;

}
