package com.mrrobot.overflow.post.controller;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.*;
import com.mrrobot.overflow.post.model.CommentResponse;
import com.mrrobot.overflow.post.model.PostBody;
import com.mrrobot.overflow.post.model.PostResponse;
import com.mrrobot.overflow.post.service.*;
import com.mrrobot.overflow.profile.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("post")
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    TopicService topicService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    PostVoteService voteService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    Logger log = LoggerFactory.getLogger("debug-logger");

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> getPosts() {
        Response response = new Response();

        List<Post> posts = postService.findAll();

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Post(s) fetched successfully.");
        response.setData(posts);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> getPostById(@PathVariable("postId") Long postId) throws NotFoundException {
        Response response = new Response();

        Optional<Post> postOptional = postService.findById(postId);

        if (postOptional.isEmpty()) {
            response.setCode(ResponseStatus.NOT_FOUND.value());
            response.setMessage("Post not found!");
        }

        List<Comment> comments = commentService.findByPostId(postId);
        List<Like> likes = likeService.findByPostId(postId);

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Post(s) fetched successfully.");
        response.setData(getPostResponseWithCommentsAndLikes(postOptional.get(), comments, likes));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> getRecentPosts(@RequestParam(required = false, defaultValue = "0", name = "page") String page) {
        Response response = new Response();

        List<Post> posts = postService.findResent(Integer.parseInt(page));

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Post(s) fetched successfully.");
        response.setData(getPostListResponse(posts));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/hot")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> getHotPosts(@RequestParam(required = false, defaultValue = "0", name = "page") String page) {

        Response response = new Response();

        List<Topic> topics = topicService.findAllHotTopics();

        List<Post> posts = postService.findByTopics(topics, Integer.valueOf(page));

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Post(s) fetched successfully.");
        response.setData(posts);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/query/{query}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> getPostsByQuery(@PathVariable("query") String query,
                                                    @RequestParam(required = false, defaultValue = "0", name = "page") String page) {
        Response response = new Response();

        List<Post> posts = postService.findByQuery(query, Integer.parseInt(page));

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Post(s) fetched successfully.");
        response.setData(posts);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/topic/{topic}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> getPostsByTopic(@PathVariable("topic") String topic,
                                                    @RequestParam(required = false, defaultValue = "0", name = "page") String page) {
        Response response = new Response();

        List<Topic> topics = topicService.findByTopicName(topic);

        List<Post> posts = postService.findByTopics(topics, Integer.valueOf(page));

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Post(s) fetched successfully.");
        response.setData(posts);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> save(@NotNull @RequestBody PostBody postBody) {
        Response response = new Response();

        try {

            Long userId = userService.getUserData().getUserId();

            Set<String> strTopics = postBody.getTopics();
            Set<Topic> topics = new HashSet<>();

            strTopics.forEach(t -> {
                Optional<Topic> topicOptional = topicService.findByName(t);

                if (topicOptional.isPresent()) {
                    topics.add(topicOptional.get());
                } else {
                    Topic topic = new Topic();
                    topic.setName(t);
                    topic.setCreatedDate(new Date());
                    topic.setCreatedBy(userId);
                    try {
                        topics.add(topicService.save(topic));
                    } catch (AlreadyExitsException e) {
                        throw new RuntimeException(t + "already exists!");
                    }
                }

            });

            Post post = new Post(postBody.getTitle(), postBody.getDescription(), userId, topics);

            post.setGroupId(postBody.getGroupId());

            if (post.getGroupId() != 0) {
                Group group = groupService.findById(post.getGroupId());
                if (group.getCreatedBy() != userId) {
                    if (!group.getUsers().stream().anyMatch(user -> user.getId() == userId)) {
                        throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "User is not subscribed in this group!");
                    }
                }

            }

            Post postData = postService.save(post);

            topics.forEach(topic -> {
                topic.setHit(topic.getHit() + 1);
                try {
                    topicService.update(topic);
                } catch (NotFoundException e) {
                    return;
                }
            });

            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("Post saved successfully.");
            response.setData(getPostResponseWithCommentsAndLikes(postData, new ArrayList<>(), new ArrayList<>()));

        } catch (AlreadyExitsException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());

        } catch (RuntimeException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(ResponseStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());

        } catch (NotFoundException e) {
            log.error("errorMessage={}", e.getMessage());
            response.setCode(ResponseStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/like/{postId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> likePost(@PathVariable("postId") long postId) {

        Response response = new Response();
        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Liked successfully!");

        try {

            Long userId = userService.getUserData().getUserId();

            Optional<Post> postOptional = postService.findById(postId);

            if (postOptional.isEmpty())
                throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "Post not found!");

            if (userService.findById(userId).isEmpty())
                throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "User not found!");

            Like like = new Like();
            like.setLikedBy(userId);
            like.setPost(postOptional.get());

            likeService.save(like);

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

    @GetMapping("/vote/{postId}/{isUpVote}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> votePost(@PathVariable("postId") long postId, @PathVariable("isUpVote") int isUpVote) {

        Response response = new Response();
        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Voted successfully!");

        try {

            Long userId = userService.getUserData().getUserId();

            Optional<Post> postOptional = postService.findById(postId);

            if (postOptional.isEmpty())
                throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "Post not found!");

            if (userService.findById(userId).isEmpty())
                throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "User not found!");

            PostVote vote = new PostVote();
            vote.setVoteBy(userId);
            if (isUpVote == 1)
                vote.setIsUpVote(true);
            else
                vote.setIsUpVote(false);
            vote.setPost(postOptional.get());

            voteService.save(vote);

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

    private PostResponse getPostResponseWithCommentsAndLikes(Post post, List<Comment> comments, List<Like> likes) throws NotFoundException {
        PostResponse response = modelMapper.map(post, PostResponse.class);
        response.setComments(getCommentResponse(comments));
        response.setLikes(likes);
        response.setTotalLikes(likes.size());
        response.setTotalComments(comments.size());
        response.setPostedUser(userService.findByUserId(post.getPostedBy()).getUsername());
        return response;
    }

    private List<CommentResponse> getCommentResponse(List<Comment> comments) {

        List<CommentResponse> responses = new ArrayList<>();

        comments.forEach(comment -> {
            CommentResponse response = modelMapper.map(comment, CommentResponse.class);
            try {
                response.setCommentedUser(userService.findByUserId(comment.getCommentedBy()).getUsername());
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            responses.add(response);
        });
        return responses;
    }

    private PostResponse getPostResponse(Post post, List<Comment> comments, List<Like> likes) throws NotFoundException {
        PostResponse response = modelMapper.map(post, PostResponse.class);
        response.setPostedUser(userService.findByUserId(post.getPostedBy()).getUsername());
        response.setTotalComments(comments.size());
        response.setTotalLikes(likes.size());
        return response;
    }

    private List<PostResponse> getPostListResponse(List<Post> posts) {
        List<PostResponse> responses = new ArrayList<>();
        posts.forEach(post -> {
            List<Comment> comments = commentService.findByPostId(post.getId());
            List<Like> likes = likeService.findByPostId(post.getId());
            try {
                responses.add(getPostResponse(post, comments, likes));
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        });
        return responses;
    }
}
