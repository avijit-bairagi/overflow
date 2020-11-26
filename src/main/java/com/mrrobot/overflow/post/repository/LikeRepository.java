package com.mrrobot.overflow.post.repository;

import com.mrrobot.overflow.post.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByLikedByAndPostId(Long likedBy, Long postId);

    List<Like> findByPostId(Long postId);

    List<Like> findByLikedBy(Long id);
}
