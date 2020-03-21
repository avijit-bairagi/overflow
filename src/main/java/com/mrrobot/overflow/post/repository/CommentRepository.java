package com.mrrobot.overflow.post.repository;

import com.mrrobot.overflow.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long id);

    Optional<Comment> findByDescription(String description);

    List<Comment> findByPostId(Long postId);
}
