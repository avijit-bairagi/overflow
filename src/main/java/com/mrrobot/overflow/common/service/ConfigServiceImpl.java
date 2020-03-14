package com.mrrobot.overflow.common.service;

import com.mrrobot.overflow.common.entity.Config;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.repository.ConfigRepository;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    ConfigRepository configRepository;


    @Override
    public Config getConfig() throws NotFoundException {

        List<Config> configs = configRepository.findAll();

        if (configs.isEmpty())
            throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "App config not found!");

        return configs.get(0);
    }
}
