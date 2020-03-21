package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.post.entity.PostVote;

public interface PostVoteService {

    PostVote save(PostVote vote) throws AlreadyExitsException, NotFoundException;
}
