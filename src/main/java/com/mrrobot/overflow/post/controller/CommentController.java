package com.mrrobot.overflow.post.controller;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Comment;
import com.mrrobot.overflow.post.entity.CommentVote;
import com.mrrobot.overflow.post.entity.Post;
import com.mrrobot.overflow.post.model.CommentBody;
import com.mrrobot.overflow.post.service.CommentService;
import com.mrrobot.overflow.post.service.CommentVoteService;
import com.mrrobot.overflow.post.service.PostService;
import com.mrrobot.overflow.profile.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    UserService userService;

    @Autowired
    CommentVoteService voteService;

    Logger log = LoggerFactory.getLogger("debug-logger");

    @PostMapping("/{postId}")
    public ResponseEntity<Response> save(@NotNull @RequestBody CommentBody commentBody, @PathVariable("postId") Long postId) {
        Response response = new Response();

        Optional<Post> postOptional = postService.findById(postId);

        if (postOptional.isEmpty()) {

            log.error("errorMessage={}", "Post not found!");
            response.setCode(ResponseStatus.NOT_FOUND.value());
            response.setMessage("Post not found!");
        }

        Comment comment = new Comment();
        comment.setDescription(commentBody.getDescription());
        comment.setCommentedBy(userService.getUserData().getUserId());
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

    @GetMapping("/vote/{commentId}/{isUpVote}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> voteComment(@PathVariable("commentId") long postId, @PathVariable("isUpVote") int isUpVote) {

        Response response = new Response();

        try {

            Long userId = userService.getUserData().getUserId();

            Optional<Comment> commentOptional = commentService.findById(postId);

            if (commentOptional.isEmpty())
                throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "Comment not found!");

            if (userService.findById(userId).isEmpty())
                throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "User not found!");

            CommentVote vote = new CommentVote();
            vote.setVoteBy(userId);
            if (isUpVote == 1)
                vote.setIsUpVote(true);
            else
                vote.setIsUpVote(false);
            vote.setComment(commentOptional.get());

            voteService.save(vote);

            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("Voted successfully!");

        } catch (AlreadyExitsException e) {
            log.error("ErrorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
        } catch (NotFoundException e) {
            log.error("ErrorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
        }

        if (!response.getCode().equalsIgnoreCase(ResponseStatus.SUCCESS.value()))
            ResponseEntity.badRequest().body(response);
        return ResponseEntity.ok(response);
    }

}
