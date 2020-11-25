package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.post.entity.Post;
import com.mrrobot.overflow.post.entity.Topic;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Optional<Post> findById(Long id);

    List<Post> findAll();

    List<Post> findResent(int page);

    List<Post> findByQuery(String query, int page);

    List<Post> findByGroupId(Long groupId);

    List<Post> findByTopics(List<Topic> topics, int page);

    Post save(Post post) throws AlreadyExitsException, NotFoundException;

    Post update(Post post) throws NotFoundException;
}
