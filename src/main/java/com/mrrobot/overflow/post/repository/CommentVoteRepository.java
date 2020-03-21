package com.mrrobot.overflow.post.repository;

import com.mrrobot.overflow.post.entity.CommentVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {

    Optional<CommentVote> findByVoteByAndCommentId(Long voteBy, Long commentId);
}