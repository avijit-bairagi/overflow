package com.mrrobot.overflow.boot.service;

import com.mrrobot.overflow.boot.model.Config;
import com.mrrobot.overflow.boot.repository.BootRepository;
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
