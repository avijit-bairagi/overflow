package com.mrrobot.overflow.common.controller;

import com.mrrobot.overflow.security.service.BootService;
import com.mrrobot.overflow.common.entity.Config;
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

        log.debug("getApplicationConfigs(): start");

        List<Config> configs = bootService.getConfig();

        log.debug("getApplicationConfigs(): end");

        return ResponseEntity.status(HttpStatus.OK).body(configs);
    }
}
