package com.mrrobot.overflow.post.repository;

import com.mrrobot.overflow.post.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByVoteByAndPostId(Long voteBy, Long postId);
}
