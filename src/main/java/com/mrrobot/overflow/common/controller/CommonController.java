package com.mrrobot.overflow.common.controller;

import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.model.ConfigResponse;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.common.service.ConfigService;
import com.mrrobot.overflow.common.entity.Config;
import com.mrrobot.overflow.post.repository.GroupRepository;
import com.mrrobot.overflow.post.repository.PostRepository;
import com.mrrobot.overflow.profile.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("config/")
public class CommonController {

    @Autowired
    ConfigService bootService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    PostRepository postRepository;

    Logger log = LoggerFactory.getLogger("debug-logger");

    @GetMapping
    public ResponseEntity<Response> getApplicationConfigs() {

        log.debug("getApplicationConfigs(): start");
        Response response = new Response();
        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Application config fetched successfully.");

        try {
            Config appConfig = bootService.getConfig();

            ConfigResponse configResponse = new ConfigResponse();
            configResponse.setConfig(appConfig);
            configResponse.setTotalGroup(groupRepository.findAll().size());
            configResponse.setTotalPost(postRepository.findAll().size());
            configResponse.setTotalUser(userRepository.findAll().size());

            response.setData(configResponse);
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
