package com.mrrobot.overflow.post.repository;

import com.mrrobot.overflow.post.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);

    List<Group> findByCreatedBy(Long createdBy);

    List<Group> findByUsersId(Long createdBy);
}
