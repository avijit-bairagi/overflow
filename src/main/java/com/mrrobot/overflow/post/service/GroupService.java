package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.post.entity.Group;

import java.util.List;

public interface GroupService {

    Group findById(Long id) throws NotFoundException;

    List<Group> findAll();

    Group save(Group group) throws AlreadyExitsException;

    Group update(Group group) throws NotFoundException;

    void subscribe(Long userId, Long groupId) throws NotFoundException, AlreadyExitsException;

    void unsubscribe(Long userId, Long groupId) throws NotFoundException;
}
