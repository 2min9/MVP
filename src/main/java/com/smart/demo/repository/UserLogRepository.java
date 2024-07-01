package com.smart.demo.repository;

import com.smart.demo.entity.UserLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogRepository extends JpaRepository<UserLogEntity, Integer> {
}
