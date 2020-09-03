package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Post;
import com.mrrobot.overflow.post.entity.PostVote;
import com.mrrobot.overflow.post.repository.PostVoteRepository;
import com.mrrobot.overflow.profile.entity.Profile;
import com.mrrobot.overflow.profile.service.ProfileService;
import com.mrrobot.overflow.rank.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostVoteServiceImpl implements PostVoteService {

    @Autowired
    PostVoteRepository voteRepository;

    @Autowired
    RankService rankService;

    @Autowired
    PostService postService;

    @Autowired
    ProfileService profileService;

    @Override
    public PostVote save(PostVote vote) throws AlreadyExitsException, NotFoundException {

        if (voteRepository.findByVoteByAndPostId(vote.getVoteBy(), vote.getPost().getId()).isPresent()) {
            throw new AlreadyExitsException(ResponseStatus.ALREADY_LIKED.value(), "Already voted!");
        }

        Post post = vote.getPost();

        Double point = post.getPoint() + rankService.getPoint(vote.getVoteBy(), vote.getIsUpVote());

        post.setPoint(point);

        if(vote.getIsUpVote()){
            post.setHit(post.getHit() +1);
        }else{
            post.setHit(post.getHit() -1);
        }

        postService.update(post);

        if (vote.getVoteBy() != vote.getPost().getPostedBy()) {

            Optional<Profile> profileOptional = profileService.findByUserId(vote.getPost().getPostedBy());

            if (profileOptional.isEmpty())
                throw new NotFoundException(ResponseStatus.NOT_FOUND.value(), "Profile not found!");

            Profile profile = profileOptional.get();
            profile.setPoint(profile.getPoint() + point);

            profileService.update(profile);
        }

        return voteRepository.save(vote);
    }
}
