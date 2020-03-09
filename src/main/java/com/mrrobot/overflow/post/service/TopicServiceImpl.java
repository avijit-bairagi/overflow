package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Topic;
import com.mrrobot.overflow.post.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    TopicRepository topicRepository;

    @Override
    public Optional<Topic> findByName(String name) {
        return topicRepository.findByName(name);
    }

    @Override
    public List<Topic> findAll() {
        return topicRepository.findAll();
    }

    @Override
    public Topic save(Topic topic) throws AlreadyExitsException {

        if (topicRepository.findByName(topic.getName()).isPresent()) {

            throw new AlreadyExitsException(ResponseStatus.ALREADY_EXITS.value(), topic.getName() + " already exits!");
        }

        return topicRepository.save(topic);
    }

    @Override
    public Topic update(Topic topic) throws NotFoundException {

        if (topicRepository.findById(topic.getId()).isEmpty()) {

            throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "Topic not found!");
        }

        return topicRepository.save(topic);
    }
}
