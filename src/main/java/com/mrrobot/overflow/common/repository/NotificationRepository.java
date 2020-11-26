package com.mrrobot.overflow.common.repository;

import com.mrrobot.overflow.common.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedTimeDesc(Long id);

    Optional<Notification> findById(Long id);
}
