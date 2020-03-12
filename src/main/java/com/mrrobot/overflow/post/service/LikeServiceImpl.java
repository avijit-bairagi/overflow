package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Like;
import com.mrrobot.overflow.post.repository.LikeRepository;
import com.mrrobot.overflow.post.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    LikeRepository likeRepository;

    @Override
    public Like save(Like like) throws AlreadyExitsException {

        if (likeRepository.findByLikedByAndPostId(like.getLikedBy(), like.getPost().getId()).isPresent()) {
            throw new AlreadyExitsException(ResponseStatus.ALREADY_LIKED.value(), "Already liked!");
        }

        return likeRepository.save(like);
    }

    @Override
    public List<Like> findByPostId(Long postId) {
        return likeRepository.findByPostId(postId);
    }
}
