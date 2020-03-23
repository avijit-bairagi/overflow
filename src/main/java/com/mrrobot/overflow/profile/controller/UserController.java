package com.mrrobot.overflow.profile.controller;

import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.model.ProfileResponse;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.profile.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    private Logger log = LoggerFactory.getLogger("debug-logger");

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getAll() {

        Response response = new Response();

        List<ProfileResponse> responseList = userService.findAll();

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("User(s) fetched successfully!");
        response.setData(responseList);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("rank")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getUserByRanking(@RequestParam(required = false, defaultValue = "0", name = "page") String page) {

        Response response = new Response();

        List<ProfileResponse> responseList = userService.findAllByRanking(Integer.parseInt(page));

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("User(s) fetched successfully!");
        response.setData(responseList);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> getProfile(@PathVariable("userId") Long userId) {

        Response response = new Response();

        try {
            ProfileResponse profileResponse = userService.findByUserId(userId);

            response.setData(profileResponse);
            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("User fetched successfully.");

        } catch (NotFoundException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
        }


        if (response.getCode().equalsIgnoreCase(ResponseStatus.SUCCESS.value()))
            return ResponseEntity.ok().body(response);
        else
            return ResponseEntity.badRequest().body(response);
    }

}
