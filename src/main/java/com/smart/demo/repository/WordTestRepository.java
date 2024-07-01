package com.smart.demo.repository;

import com.smart.demo.entity.WordTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordTestRepository extends JpaRepository<WordTestEntity, Integer> {
}
