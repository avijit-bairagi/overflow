package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Comment;
import com.mrrobot.overflow.post.entity.CommentVote;
import com.mrrobot.overflow.post.repository.CommentVoteRepository;
import com.mrrobot.overflow.profile.entity.Profile;
import com.mrrobot.overflow.profile.service.ProfileService;
import com.mrrobot.overflow.rank.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentVoteServiceImpl implements CommentVoteService {

    @Autowired
    CommentVoteRepository voteRepository;

    @Autowired
    RankService rankService;

    @Autowired
    CommentService commentService;

    @Autowired
    ProfileService profileService;

    @Override
    public CommentVote save(CommentVote vote) throws AlreadyExitsException, NotFoundException {

        if (voteRepository.findByVoteByAndCommentId(vote.getVoteBy(), vote.getComment().getId()).isPresent()) {
            throw new AlreadyExitsException(ResponseStatus.ALREADY_LIKED.value(), "Already voted!");
        }

        Comment comment = vote.getComment();

        Double point = rankService.getPoint(vote.getVoteBy(), vote.getIsUpVote());

        comment.setPoint(comment.getPoint() + point);

        commentService.update(comment);

        if (vote.getVoteBy() != vote.getComment().getCommentedBy()) {

            Optional<Profile> profileOptional = profileService.findByUserId(vote.getComment().getCommentedBy());

            if (profileOptional.isEmpty())
                throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "Profile not found!");

            Profile profile = profileOptional.get();
            profile.setPoint(profile.getPoint() + point);

            profileService.update(profile);

        }

        return voteRepository.save(vote);
    }
}
