package com.mrrobot.overflow.post.controller;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Group;
import com.mrrobot.overflow.post.model.GroupBody;
import com.mrrobot.overflow.post.service.GroupService;
import com.mrrobot.overflow.profile.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("group/")
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    UserService userService;

    Logger log = LoggerFactory.getLogger("debug-logger");

    @PostMapping
    public ResponseEntity<Response> save(@NotNull @RequestBody GroupBody groupBody) {

        Response response = new Response();

        Group group = new Group();
        group.setName(groupBody.getName());
        group.setDescription(groupBody.getDescription());
        group.setCreatedBy(userService.getUserData().getUserId());

        try {
            Group groupData = groupService.save(group);

            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("Group saved successfully!");
            response.setData(groupData);

        } catch (AlreadyExitsException e) {
            log.error("ErrorMessage={}", e.getMessage());

            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
        }

        if (response.getCode().equalsIgnoreCase(ResponseStatus.SUCCESS.value()))
            return ResponseEntity.ok().body(response);
        else
            return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/subscribe/{groupId}")
    public ResponseEntity<Response> subscribe(@PathVariable("groupId") Long groupId) {

        Response response = new Response();

        try {

            groupService.subscribe(userService.getUserData().getUserId(), groupId);
            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("Subscribed successfully!");

        } catch (AlreadyExitsException e) {
            log.error("ErrorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());

        } catch (NotFoundException e) {
            log.error("ErrorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
        }

        if (response.getCode().equalsIgnoreCase(ResponseStatus.SUCCESS.value()))
            return ResponseEntity.ok().body(response);
        else
            return ResponseEntity.badRequest().body(response);

    }

    @GetMapping()
    public ResponseEntity<Response> getAll() {

        Response response = new Response();

        List<Group> groups = groupService.findAll();

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Group(s) fetched successfully!");
        response.setData(groups);

        return ResponseEntity.ok().body(response);
    }
}
