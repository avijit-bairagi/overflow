package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.post.entity.Post;

import java.util.List;

public interface PostService {
    List<Post> findAll();

    Post save(Post post) throws AlreadyExitsException;

    Post update(Post post) throws NotFoundException;
}
