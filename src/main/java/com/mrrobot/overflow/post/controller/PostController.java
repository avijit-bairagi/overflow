package com.mrrobot.overflow.post.controller;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Post;
import com.mrrobot.overflow.post.entity.Topic;
import com.mrrobot.overflow.post.model.PostBody;
import com.mrrobot.overflow.post.service.PostService;
import com.mrrobot.overflow.post.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("post")
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    TopicService topicService;

    Logger log = LoggerFactory.getLogger("debug-logger");

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getPosts() {
        Response response = new Response();

        List<Post> posts = postService.findAll();

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Post(s) fetched successfully.");
        response.setData(posts);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getRecentPosts(@RequestParam(required = false, defaultValue = "0", name = "limit") String limit) {
        Response response = new Response();

        List<Post> posts = postService.findAll();

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Post(s) fetched successfully.");
        response.setData(posts);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/hot")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getHotPosts(@RequestParam(required = false, defaultValue = "0", name = "limit") String limit) {
        Response response = new Response();

        List<Post> posts = postService.findAll();

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Post(s) fetched successfully.");
        response.setData(posts);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/query/{query}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getPostsByQuery(@PathVariable("query") String query,
                                                    @RequestParam(required = false, defaultValue = "0", name = "limit") String limit) {
        Response response = new Response();

        List<Post> posts = postService.findAll();

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Post(s) fetched successfully.");
        response.setData(posts);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/topic/{topic}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getPostsByTopic(@PathVariable("topic") String topic,
                                                    @RequestParam(required = false, defaultValue = "0", name = "limit") String limit) {
        Response response = new Response();

        List<Post> posts = postService.findAll();

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Post(s) fetched successfully.");
        response.setData(posts);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> save(@NotNull @RequestBody PostBody postBody) {
        Response response = new Response();

        try {

            Set<String> strTopics = postBody.getTopics();
            Set<Topic> topics = new HashSet<>();

            strTopics.forEach(t -> {
                Topic topic = topicService.findByName(t)
                        .orElseThrow(() -> new RuntimeException(t + " not found."));
                topics.add(topic);
            });

            Post post = postService.save(new Post(postBody.getTitle(), postBody.getDescription(), postBody.getPostedBy(), topics));
            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("Post saved successfully.");
            response.setData(post);
        } catch (AlreadyExitsException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
        } catch (RuntimeException e) {

            log.error("errorMessage={}", e.getMessage());
            response.setCode(ResponseStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
        }

        if (response.getCode().equalsIgnoreCase(ResponseStatus.SUCCESS.value()))
            return ResponseEntity.ok().body(response);
        else
            return ResponseEntity.badRequest().body(response);
    }
}
