package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Comment;
import com.mrrobot.overflow.post.repository.CommentRepository;
import com.mrrobot.overflow.profile.entity.Profile;
import com.mrrobot.overflow.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Configuration
@Service
public class CommentServiceImpl implements CommentService {

    @Value("${post.defaultCommentPoint}")
    int defaultCommentPoint;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ProfileService profileService;

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Optional<Comment> findByDescription(String description) {
        return commentRepository.findByDescription(description);
    }

    @Override
    public Comment save(Comment comment) throws AlreadyExitsException, NotFoundException {

        if (commentRepository.findByDescription(comment.getDescription()).isPresent()) {

            throw new AlreadyExitsException(ResponseStatus.ALREADY_EXITS.value(), "Comment already exits!");
        }

        if (comment.getCommentedBy() != comment.getPost().getPostedBy()) {

            Optional<Profile> profileOptional = profileService.findByUserId(comment.getCommentedBy());

            if (profileOptional.isEmpty())
                throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "Profile not found!");

            Profile profile = profileOptional.get();
            profile.setPoint(profile.getPoint() + defaultCommentPoint);

            profileService.update(profile);
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
