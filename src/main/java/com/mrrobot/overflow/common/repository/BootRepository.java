package com.mrrobot.overflow.common.repository;

import com.mrrobot.overflow.common.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BootRepository extends JpaRepository<Config, Long> {
}
