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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> getProfile(@PathVariable("userId") Long userId) {

        Response response = new Response();

        Optional<User> userOptional = userService.findById(userId);

        if (userOptional.isPresent()) {
            Optional<Profile> profileOptional = profileService.findByUserId(userId);

            if (profileOptional.isPresent()) {
                response.setCode(ResponseStatus.SUCCESS.value());
                response.setMessage("User data fetch successfully!");
                response.setData(getProfileData(userOptional.get(), profileOptional.get()));

                return ResponseEntity.ok().body(response);
            }
        }

        response.setCode(ResponseStatus.NOT_FOUND.value());
        response.setMessage("User data not found!");
        return ResponseEntity.badRequest().body(response);
    }

    private ProfileResponse getProfileData(User user, Profile profile) {

        ProfileResponse response = modelMapper.map(profile, ProfileResponse.class);
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return response;
    }

}
