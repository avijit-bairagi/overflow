package com.mrrobot.overflow.security.controller;

import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.profile.model.Role;
import com.mrrobot.overflow.profile.model.User;
import com.mrrobot.overflow.profile.service.RoleService;
import com.mrrobot.overflow.profile.service.UserService;
import com.mrrobot.overflow.security.jwt.JwtProvider;
import com.mrrobot.overflow.security.model.JwtResponse;
import com.mrrobot.overflow.security.model.LoginBody;
import com.mrrobot.overflow.security.model.RegistrationBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
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
    RoleService roleService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginBody loginBody) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginBody.getUsername(),
                        loginBody.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);

        Response response = new Response();
        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Login successful!");
        response.setData(new JwtResponse(jwt));

        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@Valid @RequestBody RegistrationBody registrationBody) {

        Response response = new Response();

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

        User user = new User(registrationBody.getName(), registrationBody.getUsername(),
                registrationBody.getEmail(), encoder.encode(registrationBody.getPassword()));

        Set<String> strRoles = registrationBody.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(r -> {

            Role role = roleService.findByName("ROLE_" + r)
                    .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
            roles.add(role);
        });

        user.setRoles(roles);
        User data = userService.save(user);

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("User registered successfully!");
        response.setData(data);

        return ResponseEntity.ok().body(response);
    }
}