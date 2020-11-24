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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.mrrobot.overflow.common.utils.Constants.USER_ROLE;

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

        HashMap<String, String> errors = new HashMap<>();

        Response response = new Response();

        try {

            User user = commonService.checkUser(loginBody.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginBody.getUsername(), loginBody.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtProvider.generateJwtToken(authentication);

            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("Login successful!");
            response.setData(new JwtResponse(jwt, user.getId()));

        } catch (BadCredentialsException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(ResponseStatus.BAD_CREDENTIALS.value());
            errors.put("password", "Wrong password");
            response.setMessage(e.getMessage());
        } catch (AuthenticationException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(ResponseStatus.AUTH_ERROR.value());
            response.setMessage(e.getMessage());
        } catch (NotFoundException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            errors.put("username", "User not found");
            response.setMessage(e.getMessage());
        } catch (UserLoginException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
        }

        response.setErrors(errors);

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

            Role role = roleService.findByName(USER_ROLE)
                    .orElseThrow(() -> new RuntimeException(USER_ROLE + " - role not found!"));
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
            User userData = userService.save(user);

            Profile profile = new Profile(registrationBody.getFirstName(), registrationBody.getLastName(),
                    registrationBody.getAddressLine(), registrationBody.getCity(), registrationBody.getPhoneNo(), registrationBody.getIsOpenForJob());
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

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/find-by-username/{username}")
    public ResponseEntity<Response> findByUsername(@PathVariable("username") String username) {

        log.debug("findByUsername(): start");

        Response response = new Response();

        try {

            if(!userService.existsByUsername(username)){
                throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "User not found.");
            }
            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("User found.");

        } catch (NotFoundException e) {

            log.error("errorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
        }

        log.debug("findByUsername(): end");

        if (response.getCode().equalsIgnoreCase(ResponseStatus.SUCCESS.value()))
            return ResponseEntity.ok().body(response);
        else
            return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/find-by-email/{email}")
    public ResponseEntity<Response> findByEmail(@PathVariable("email") String email) {

        log.debug("findByEmail(): start");

        Response response = new Response();

        try {

            if(!userService.existsByEmail(email)){
                throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "User not found.");
            }

            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("User found.");

        } catch (NotFoundException e) {

            log.error("errorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
        }

        log.debug("findByEmail(): end");

        if (response.getCode().equalsIgnoreCase(ResponseStatus.SUCCESS.value()))
            return ResponseEntity.ok().body(response);
        else
            return ResponseEntity.badRequest().body(response);
    }

}