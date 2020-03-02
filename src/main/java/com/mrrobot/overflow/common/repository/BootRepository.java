package com.mrrobot.overflow.boot.repository;

import com.mrrobot.overflow.boot.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BootRepository extends JpaRepository<Config, Long> {
}
