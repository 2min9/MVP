package com.smart.demo.repository;

import com.smart.demo.entity.LoginLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginLogRepository extends JpaRepository<LoginLogEntity, Integer> {
}
