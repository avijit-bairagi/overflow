package com.mrrobot.overflow.profile.controller;

import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.model.ProfileResponse;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.profile.entity.Profile;
import com.mrrobot.overflow.profile.entity.User;
import com.mrrobot.overflow.profile.model.ProfileUpdateRequestDto;
import com.mrrobot.overflow.profile.service.ProfileService;
import com.mrrobot.overflow.profile.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    Logger log = LoggerFactory.getLogger("debug-logger");

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> getProfileById(@PathVariable("userId") Long userId) {

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

    @GetMapping("/")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> getProfile() {

        Response response = new Response();

        Long userId = userService.getUserData().getUserId();

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

    @PostMapping("/update/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> updatePhoneVisibility(@Valid @RequestBody ProfileUpdateRequestDto requestDto, @PathVariable("userId") Long userId) {

        Response response = new Response();

        try {

            if(userId != requestDto.getUserId())
                throw new RuntimeException("User not found.");

            Profile profile = profileService.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("User not found."));

            profile.setPhoneNoVisibility(requestDto.getPhoneNoVisibility());
            profile.setPhoneNo(requestDto.getPhoneNo());
            profile.setAddressLine(requestDto.getAddressLine());
            profile.setCity(requestDto.getCity());
            profile.setIsOpenForJob(requestDto.getIsOpenForJob());
            profile.setFirstName(requestDto.getFirstName());
            profile.setLastName(requestDto.getLastName());

            profileService.update(profile);

            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("Profile updated successfully!");
            return ResponseEntity.ok().body(response);

        } catch (NotFoundException | RuntimeException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(ResponseStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

    }

    private ProfileResponse getProfileData(User user, Profile profile) {

        ProfileResponse response = modelMapper.map(profile, ProfileResponse.class);
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return response;
    }

}
