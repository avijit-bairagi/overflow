package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Comment;
import com.mrrobot.overflow.post.entity.CommentVote;
import com.mrrobot.overflow.post.repository.CommentVoteRepository;
import com.mrrobot.overflow.rank.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentVoteServiceImpl implements CommentVoteService {

    @Autowired
    CommentVoteRepository voteRepository;

    @Autowired
    RankService rankService;

    @Autowired
    CommentService commentService;

    @Override
    public CommentVote save(CommentVote vote) throws AlreadyExitsException, NotFoundException {

        if (voteRepository.findByVoteByAndCommentId(vote.getVoteBy(), vote.getComment().getId()).isPresent()) {
            throw new AlreadyExitsException(ResponseStatus.ALREADY_LIKED.value(), "Already voted!");
        }

        Comment comment = vote.getComment();

        Double point = comment.getPoint() + rankService.getPoint(vote.getVoteBy(), vote.getIsUpVote());

        comment.setPoint(point);

        commentService.update(comment);

        return voteRepository.save(vote);
    }
}
