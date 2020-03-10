package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Comment;
import com.mrrobot.overflow.post.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Override
    public Optional<Comment> findByDescription(String description) {
        return commentRepository.findByDescription(description);
    }

    @Override
    public Comment save(Comment comment) throws AlreadyExitsException {

        if (commentRepository.findByDescription(comment.getDescription()).isPresent()) {

            throw new AlreadyExitsException(ResponseStatus.ALREADY_EXITS.value(), "Comment already exits!");
        }

        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Comment comment) throws NotFoundException {

        if (commentRepository.findByDescription(comment.getDescription()).isEmpty()) {

            throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "Comment not found!");
        }

        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}
