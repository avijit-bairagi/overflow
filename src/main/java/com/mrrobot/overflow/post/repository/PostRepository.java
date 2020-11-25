package com.mrrobot.overflow.post.repository;

import com.mrrobot.overflow.post.entity.Post;
import com.mrrobot.overflow.post.entity.Topic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByTitle(String name);

    List<Post> findByTitleContaining(String title, Pageable pageable);

    List<Post> findByGroupId(Long groupId);

    List<Post> findByTopicsIn(Collection<Topic> topics, Pageable pageable);
}
