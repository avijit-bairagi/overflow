package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.post.entity.Like;

import java.util.List;

public interface LikeService {

    Like save(Like like) throws AlreadyExitsException;

    List<Like> findByPostId(Long postId);

    void delete(Long userId, long postId) throws NotFoundException;
}
