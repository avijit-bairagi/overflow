package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Group;
import com.mrrobot.overflow.post.repository.GroupRepository;
import com.mrrobot.overflow.profile.entity.User;
import com.mrrobot.overflow.profile.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Group findById(Long id) throws NotFoundException {

        Optional<Group> groupOptional = groupRepository.findById(id);

        if (groupOptional.isEmpty())
            throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "Group not found!");
        return groupOptional.get();
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group save(Group group) throws AlreadyExitsException {

        if (groupRepository.findByName(group.getName()).isPresent())
            throw new AlreadyExitsException(ResponseStatus.ALREADY_EXITS.value(), "Group already exits!");

        return groupRepository.save(group);
    }

    @Override
    public Group update(Group group) throws NotFoundException {
        return null;
    }

    @Override
    public void subscribe(Long userId, Long groupId) throws NotFoundException, AlreadyExitsException {

        Optional<Group> groupOptional = groupRepository.findById(groupId);

        if (groupOptional.isEmpty())
            throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "Group not found!");

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty())
            throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "Group not found!");

        Group group = groupOptional.get();

        if (group.getUsers().stream().anyMatch(user -> user.getId() == userId))
            throw new AlreadyExitsException(ResponseStatus.ALREADY_EXITS.value(), "Already subscribed!");

        group.getUsers().add(userOptional.get());

        groupRepository.save(group);
    }
}
