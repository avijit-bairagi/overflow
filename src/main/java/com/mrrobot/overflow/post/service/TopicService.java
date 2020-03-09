package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.post.entity.Topic;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    Optional<Topic> findByName(String name);

    List<Topic> findAll();

    Topic save(Topic topic) throws AlreadyExitsException;

    Topic update(Topic topic) throws NotFoundException;
}
