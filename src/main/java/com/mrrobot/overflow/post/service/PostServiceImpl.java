package com.mrrobot.overflow.post.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Post;
import com.mrrobot.overflow.post.entity.Topic;
import com.mrrobot.overflow.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@Service
public class PostServiceImpl implements PostService {

    @Value("${post.defaultLimit}")
    int defaultPostLimit;

    @Autowired
    PostRepository postRepository;

    @Autowired
    TopicService topicService;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findResent(int page) {

        Pageable pageable = PageRequest.of(page, defaultPostLimit, Sort.by("createdDate").descending());

        return postRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Post> findByQuery(String query, int page) {

        Pageable pageable = PageRequest.of(page, defaultPostLimit, Sort.by("createdDate").descending());

        return postRepository.findByTitleContaining(query, pageable);
    }

    @Override
    public List<Post> findByTopics(List<Topic> topics, int page) {

        Pageable pageable = PageRequest.of(page, defaultPostLimit, Sort.by("createdDate").descending());

        return postRepository.findByTopicsIn(topics, pageable);
    }

    @Override
    public Post save(Post post) throws AlreadyExitsException {

        if (postRepository.findByTitle(post.getTitle()).isPresent()) {

            throw new AlreadyExitsException(ResponseStatus.ALREADY_EXITS.value(), "Post already exits!");
        }

        return postRepository.save(post);
    }

    @Override
    public Post update(Post post) throws NotFoundException {

        if (postRepository.findById(post.getId()).isEmpty()) {

            throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "Post not found!");
        }

        return postRepository.save(post);
    }
}
