package com.mrrobot.overflow.post.controller;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.model.GroupRes;
import com.mrrobot.overflow.common.model.ProfileResponse;
import com.mrrobot.overflow.common.model.Response;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Comment;
import com.mrrobot.overflow.post.entity.Group;
import com.mrrobot.overflow.post.entity.Like;
import com.mrrobot.overflow.post.entity.Post;
import com.mrrobot.overflow.post.model.*;
import com.mrrobot.overflow.post.repository.GroupRepository;
import com.mrrobot.overflow.post.repository.PostRepository;
import com.mrrobot.overflow.post.service.CommentService;
import com.mrrobot.overflow.post.service.GroupService;
import com.mrrobot.overflow.post.service.LikeService;
import com.mrrobot.overflow.post.service.PostService;
import com.mrrobot.overflow.profile.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("group")
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ModelMapper modelMapper;

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


    @GetMapping("/subscribe/{groupId}/{userId}")
    public ResponseEntity<Response> subscribeUser(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId) {

        Response response = new Response();

        try {

            groupService.subscribe(userId, groupId);
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

    @GetMapping("/unsubscribe/{groupId}")
    public ResponseEntity<Response> unsubscribe(@PathVariable("groupId") Long groupId) {

        Response response = new Response();

        try {

            groupService.unsubscribe(userService.getUserData().getUserId(), groupId);
            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("Unsubscribed successfully!");

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

    @GetMapping("/unsubscribe/{groupId}/{userId}")
    public ResponseEntity<Response> unsubscribeUser(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId) {

        Response response = new Response();

        try {

            groupService.unsubscribe(userId, groupId);
            response.setCode(ResponseStatus.SUCCESS.value());
            response.setMessage("Unsubscribed successfully!");

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

        Long userId = userService.getUserData().getUserId();

        List<Group> myGroups = groupRepository.findByCreatedBy(userId);
        List<Group> mySubGroups = groupRepository.findByUsersId(userId);

        myGroups.addAll(mySubGroups);

        GroupRes res = new GroupRes();
        res.setGroups(convertToResponse(myGroups));
        res.setTotalGroup(myGroups.size());
        res.setTotalPgroup(myGroups.size());
        res.setTotalPost(postRepository.findByPostedByAndGroupIdNot(userId, 0l).size());

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Group(s) fetched successfully!");
        response.setData(res);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Response> getById(@PathVariable("groupId") Long groupId) throws NotFoundException {

        Response response = new Response();

        Group group = groupService.findById(groupId);

        response.setCode(ResponseStatus.SUCCESS.value());
        response.setMessage("Group fetched successfully!");
        response.setData(getResponse(group));

        return ResponseEntity.ok().body(response);
    }

    private GroupResponse getResponse(Group group) throws NotFoundException {

        ProfileResponse profile = userService.findByUserId(group.getCreatedBy());

        KeyValue keyValue = new KeyValue(profile.getUserId(), profile.getUsername());

        GroupResponse response = modelMapper.map(group, GroupResponse.class);
        response.setTotalMember(group.getUsers().size());
        List<Post> posts = postService.findByGroupId(group.getId());
        response.setTotalPost(posts.size());
        response.setCreatedByUser(profile.getUsername());
        response.setPosts(getPostListResponse(posts));

        List<KeyValue> users = new ArrayList<>();
        List<KeyValue> allUsers = new ArrayList<>();

        group.getUsers().forEach(user -> users.add(new KeyValue(user.getId(), user.getUsername())));

        users.add(keyValue);
        userService.findAll().forEach(p -> {
            if (users.stream().filter(kv ->
                    kv.getValue().equalsIgnoreCase(p.getUsername())
            ).findFirst().isEmpty())
                allUsers.add(new KeyValue(p.getUserId(), p.getUsername()));
        });

        users.remove(keyValue);

        response.setUsers(users);
        response.setAllUsers(allUsers);
        return response;
    }

    private List<GroupResponse> convertToResponse(List<Group> groups) {
        List<GroupResponse> responses = new ArrayList<>();
        groups.forEach(group -> {
            GroupResponse response = modelMapper.map(group, GroupResponse.class);
            response.setTotalMember(group.getUsers().size());
            response.setTotalPost(postService.findByGroupId(group.getId()).size());
            try {
                response.setCreatedByUser(userService.findByUserId(group.getCreatedBy()).getUsername());
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            responses.add(response);
        });

        return responses;
    }

    private PostResponse getPostResponseWithCommentsAndLikes(Post post, List<Comment> comments, List<Like> likes) {
        PostResponse response = modelMapper.map(post, PostResponse.class);
        response.setComments(getCommentResponse(comments));
        response.setLikes(likes);
        response.setTotalLikes(likes.size());
        response.setTotalComments(comments.size());
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
