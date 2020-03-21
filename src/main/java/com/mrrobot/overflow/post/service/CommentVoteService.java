package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.post.entity.CommentVote;

public interface CommentVoteService {

    CommentVote save(CommentVote vote) throws AlreadyExitsException, NotFoundException;
}