package com.mrrobot.overflow.post.repository;

import com.mrrobot.overflow.post.entity.Topic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Optional<Topic> findByName(String name);

    List<Topic> findByNameContaining(String title, Pageable pageable);
}
