package com.mrrobot.overflow.security.service;

import com.mrrobot.overflow.common.entity.Config;
import com.mrrobot.overflow.common.repository.BootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BootServiceImpl implements BootService{

    @Autowired
    BootRepository bootRepository;

    @Override
    public List<Config> getConfig() {
        return bootRepository.findAll();
    }
}
