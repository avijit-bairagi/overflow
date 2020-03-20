package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.post.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Optional<Post> findById(Long id);

    List<Post> findAll();

    List<Post> findResent(int page);

    List<Post> findByHotTopics(int pag);

    Post save(Post post) throws AlreadyExitsException;

    Post update(Post post) throws NotFoundException;
}
