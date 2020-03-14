package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.post.entity.Vote;

public interface VoteService {

    Vote save(Vote vote) throws AlreadyExitsException, NotFoundException;
}
