package com.mrrobot.overflow.security.controller;

import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.exception.UserLoginException;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.service.CommonService;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.profile.entity.Profile;
import com.mrrobot.overflow.profile.entity.Role;
import com.mrrobot.overflow.profile.entity.User;
import com.mrrobot.overflow.profile.model.ChangePasswordBody;
import com.mrrobot.overflow.profile.model.LoginBody;
import com.mrrobot.overflow.profile.model.RegistrationBody;
import com.mrrobot.overflow.profile.service.ProfileService;
import com.mrrobot.overflow.profile.service.RoleService;
import com.mrrobot.overflow.profile.service.UserService;
import com.mrrobot.overflow.security.jwt.JwtProvider;
import com.mrrobot.overflow.security.model.JwtResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    ProfileService profileService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    CommonService commonService;

    Logger log = LoggerFactory.getLogger("debug-logger");

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginBody loginBody) {

        log.debug("authenticateUser(): start");

        Response response = new Response();

        try {

            commonService.checkUser(loginBody.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginBody.getUsername(), loginBody.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtProvider.generateJwtToken(authentication);

            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("Login successful!");
            response.setData(new JwtResponse(jwt));

        } catch (BadCredentialsException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(ResponseStatus.BAD_CREDENTIALS.value());
            response.setMessage(e.getMessage());
        } catch (AuthenticationException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(ResponseStatus.AUTH_ERROR.value());
            response.setMessage(e.getMessage());
        } catch (NotFoundException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
        } catch (UserLoginException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
        }

        log.debug("authenticateUser(): end");

        if (response.getCode().equalsIgnoreCase(ResponseStatus.SUCCESS.value()))
            return ResponseEntity.ok().body(response);
        else
            return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@Valid @RequestBody RegistrationBody registrationBody) {

        log.debug("registerUser(): start");

        Response response = new Response();

        try {
            if (userService.existsByUsername(registrationBody.getUsername())) {

                response.setCode(ResponseStatus.ALREADY_EXITS.value());
                response.setMessage("Username is already taken!");

                return ResponseEntity.badRequest().body(response);
            }

            if (userService.existsByEmail(registrationBody.getEmail())) {

                response.setCode(ResponseStatus.ALREADY_EXITS.value());
                response.setMessage("Email is already taken!");

                return ResponseEntity.badRequest().body(response);
            }

            User user = new User(registrationBody.getFirstName(), registrationBody.getUsername(),
                    registrationBody.getEmail(), encoder.encode(registrationBody.getPassword()));

            Set<String> strRoles = registrationBody.getRole();
            Set<Role> roles = new HashSet<>();

            strRoles.forEach(r -> {

                Role role = roleService.findByName("ROLE_" + r)
                        .orElseThrow(() -> new RuntimeException(r + " role not found!"));
                roles.add(role);
            });

            user.setRoles(roles);
            User userData = userService.save(user);

            Profile profile = new Profile(registrationBody.getFirstName(), registrationBody.getLastName(),
                    registrationBody.getAddressLine(), registrationBody.getCity(), registrationBody.getPhoneNo(), registrationBody.getIsOpenForJob());
            profile.setCreatedDate(new Date());
            profile.setLevel(0);
            profile.setPoint(0d);
            profile.setUserId(userData.getId());

            profileService.save(profile);

            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("User registered successfully!");
            response.setData(userData);

        } catch (RuntimeException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(ResponseStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
        }

        log.debug("registerUser(): end");

        if (response.getCode().equalsIgnoreCase(ResponseStatus.SUCCESS.value()))
            return ResponseEntity.ok().body(response);
        else
            return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> changePassword(@Valid @RequestBody ChangePasswordBody body) {

        log.debug("changePassword(): start");

        Response response = new Response();

        try {
            Long userId = userService.getUserData().getUserId();

            Optional<User> userOptional = userService.findById(userId);
            if (userOptional.isEmpty())
                throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "User not found!");

            User user = userOptional.get();

            if (!encoder.matches(body.getOldPassword(), user.getPassword()))
                throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "Old password does not match!");

            user.setPassword(encoder.encode(body.getNewPassword()));
            userService.update(user);

            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("Password changed successfully.");

        } catch (NotFoundException e) {

            log.error("errorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
        }

        log.debug("changePassword(): end");

        if (response.getCode().equalsIgnoreCase(ResponseStatus.SUCCESS.value()))
            return ResponseEntity.ok().body(response);
        else
            return ResponseEntity.badRequest().body(response);
    }
}