package com.mrrobot.overflow.common.controller;

import com.mrrobot.overflow.common.service.BootService;
import com.mrrobot.overflow.common.model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    BootService bootService;

    Logger log = LoggerFactory.getLogger("debug-logger");

    @GetMapping
    public ResponseEntity<List<Config> > getApplicationConfigs(){
        List<Config> configs = bootService.getConfig();

        return ResponseEntity.status(HttpStatus.OK).body(configs);
    }
}
