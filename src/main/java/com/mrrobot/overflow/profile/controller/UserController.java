package com.mrrobot.overflow.profile.controller;

import com.mrrobot.overflow.common.model.ProfileResponse;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.profile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

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

}
