package com.mrrobot.overflow.profile.controller;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.profile.entity.Role;
import com.mrrobot.overflow.profile.model.RoleBody;
import com.mrrobot.overflow.profile.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    RoleService roleService;

    Logger log = LoggerFactory.getLogger("debug-logger");

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getRoles() {
        Response response = new Response();

        List<Role> roles = roleService.findAll();

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Role(s) fetched successfully.");
        response.setData(roles);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> save(@NotNull @RequestBody RoleBody roleBody) {
        Response response = new Response();

        try {
            Role role = roleService.save(new Role("ROLE_" + roleBody.getName()));
            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("Role saved successfully.");
            response.setData(role);
        } catch (AlreadyExitsException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
        }

        if (response.getCode().equalsIgnoreCase(ResponseStatus.SUCCESS.value()))
            return ResponseEntity.ok().body(response);
        else
            return ResponseEntity.badRequest().body(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> update(@NotNull @RequestBody Role roleBody) {
        Response response = new Response();

        try {
            roleBody.setName("ROLE_" + roleBody.getName());
            Role role = roleService.update(roleBody);
            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("Role updated successfully.");
            response.setData(role);
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
