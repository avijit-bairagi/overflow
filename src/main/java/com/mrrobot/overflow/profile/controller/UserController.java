package com.mrrobot.overflow.profile.controller;

import com.mrrobot.overflow.common.model.ProfileResponse;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.profile.entity.Profile;
import com.mrrobot.overflow.profile.entity.User;
import com.mrrobot.overflow.profile.service.ProfileService;
import com.mrrobot.overflow.profile.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    ProfileService profileService;

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getAll() {

        Response response = new Response();

        List<ProfileResponse> responseList = new ArrayList<>();

        List<User> users = userService.findAll();

        users.forEach(user -> {

            Optional<Profile> profileOptional = profileService.findByUserId(user.getId());

            if (profileOptional.isPresent())
                responseList.add(getProfileData(user, profileOptional.get()));

        });


        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("User(s) fetched successfully!");
        response.setData(responseList);

        return ResponseEntity.ok().body(response);
    }

    private ProfileResponse getProfileData(User user, Profile profile) {

        ProfileResponse response = modelMapper.map(profile, ProfileResponse.class);
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return response;
    }

}
