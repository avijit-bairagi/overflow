package com.mrrobot.overflow.post.controller;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Topic;
import com.mrrobot.overflow.post.model.TopicBody;
import com.mrrobot.overflow.post.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("topic")
public class TopicController {

    @Autowired
    TopicService topicService;

    Logger log = LoggerFactory.getLogger("debug-logger");

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getTopics() {
        Response response = new Response();

        List<Topic> topics = topicService.findAll();

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Topic(s) fetched successfully.");
        response.setData(topics);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> save(@NotNull @RequestBody TopicBody topicBody) {
        Response response = new Response();

        try {
            Topic topic = topicService.save(new Topic(topicBody.getName(), topicBody.getCreatedBy()));
            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("Topic saved successfully.");
            response.setData(topic);
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
}
