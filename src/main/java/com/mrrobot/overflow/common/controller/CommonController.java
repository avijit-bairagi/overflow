package com.mrrobot.overflow.common.controller;

import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.common.service.ConfigService;
import com.mrrobot.overflow.common.entity.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("config/")
public class CommonController {

    @Autowired
    ConfigService bootService;

    Logger log = LoggerFactory.getLogger("debug-logger");

    @GetMapping
    public ResponseEntity<Response> getApplicationConfigs() {

        log.debug("getApplicationConfigs(): start");
        Response response = new Response();
        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Application config fetched successfully.");

        try {
            Config appConfig = bootService.getConfig();
            response.setData(appConfig);
        } catch (NotFoundException e) {
            log.error("ErrorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
        }

        log.debug("getApplicationConfigs(): end");

        if(response.getCode().equalsIgnoreCase(ResponseStatus.SUCCESS.value()))
            return ResponseEntity.ok().body(response);
        else
            return ResponseEntity.badRequest().body(response);
    }
}
