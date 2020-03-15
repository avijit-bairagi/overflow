package com.mrrobot.overflow.post.controller;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Comment;
import com.mrrobot.overflow.post.entity.Post;
import com.mrrobot.overflow.post.model.CommentBody;
import com.mrrobot.overflow.post.service.CommentService;
import com.mrrobot.overflow.post.service.PostService;
import com.mrrobot.overflow.security.jwt.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    PostService postService;

    @Autowired
    JwtProvider jwtProvider;

    Logger log = LoggerFactory.getLogger("debug-logger");

    @PostMapping("/{postId}")
    public ResponseEntity<Response> save(@RequestHeader(name = "Authorization") String token, @NotNull @RequestBody CommentBody commentBody,
                                         @PathVariable("postId") Long postId) {
        Response response = new Response();

        Optional<Post> postOptional = postService.findById(postId);

        if (postOptional.isEmpty()) {

            log.error("errorMessage={}", "Post not found!");
            response.setCode(ResponseStatus.NOT_FOUND.value());
            response.setMessage("Post not found!");
        }

        Comment comment = new Comment();
        comment.setDescription(commentBody.getDescription());
        comment.setCommentedBy(jwtProvider.getUserData(token).getUserId());
        comment.setPost(postOptional.get());

        try {

            Comment commentData = commentService.save(comment);
            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("Commented successfully!");
            response.setData(commentData);

        } catch (AlreadyExitsException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(ResponseStatus.ALREADY_EXITS.value());
            response.setMessage("Post already exits!");
        }

        if (response.getCode().equalsIgnoreCase(ResponseStatus.SUCCESS.value()))
            return ResponseEntity.ok().body(response);
        else
            return ResponseEntity.badRequest().body(response);
    }

}
