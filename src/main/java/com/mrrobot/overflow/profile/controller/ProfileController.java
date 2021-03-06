package com.mrrobot.overflow.profile.controller;

import com.mrrobot.overflow.common.model.ProfileResponse;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.profile.entity.Profile;
import com.mrrobot.overflow.profile.entity.User;
import com.mrrobot.overflow.profile.service.ProfileService;
import com.mrrobot.overflow.profile.service.UserService;
import com.mrrobot.overflow.security.jwt.JwtProvider;
import com.mrrobot.overflow.security.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @Autowired
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;

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

    @GetMapping("/token")
    public ResponseEntity<UserData> get(@RequestHeader(name = "Authorization") String token) {

        UserData userData = jwtProvider.getUserDataToken(token);

        return ResponseEntity.ok().body(userData);
    }

    private ProfileResponse getProfileData(User u, Profile p) {
        return new ProfileResponse(u.getUsername(), u.getEmail(), p.getFirstName(), p.getLastName(),
                p.getAddressLine(), p.getCity(), p.getPhoneNo(), p.getLevel(), p.getPoint(),
                p.getIsOpenForJob(), p.getCreatedDate(), p.getUpdatedDate());
    }

}
