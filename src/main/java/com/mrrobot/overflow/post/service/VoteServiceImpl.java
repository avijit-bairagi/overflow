package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Post;
import com.mrrobot.overflow.post.entity.Vote;
import com.mrrobot.overflow.post.repository.VoteRepository;
import com.mrrobot.overflow.rank.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    RankService rankService;

    @Autowired
    PostService postService;

    @Override
    public Vote save(Vote vote) throws AlreadyExitsException, NotFoundException {

        if (voteRepository.findByVoteByAndPostId(vote.getVoteBy(), vote.getPost().getId()).isPresent()) {
            throw new AlreadyExitsException(ResponseStatus.ALREADY_LIKED.value(), "Already voted!");
        }

        Post post = vote.getPost();

        Double point = post.getPoint() + rankService.getPoint(vote.getVoteBy(), vote.getIsUpVote());

        post.setPoint(point);

        postService.update(post);

        return voteRepository.save(vote);
    }
}
