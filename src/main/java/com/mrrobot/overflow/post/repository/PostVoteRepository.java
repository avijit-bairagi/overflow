package com.mrrobot.overflow.post.repository;

import com.mrrobot.overflow.post.entity.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Long> {

    Optional<PostVote> findByVoteByAndPostId(Long voteBy, Long postId);
}
