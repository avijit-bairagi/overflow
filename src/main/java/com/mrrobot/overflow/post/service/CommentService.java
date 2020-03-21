package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.post.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(Long id);

    Optional<Comment> findByDescription(String description);

    Comment save(Comment comment) throws AlreadyExitsException;

    Comment update(Comment comment) throws NotFoundException;

    List<Comment> findByPostId(Long postId);
}
