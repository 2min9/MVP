package com.smart.demo.repository;

import com.smart.demo.entity.WordTestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordTestRepository extends JpaRepository<WordTestEntity, Integer> {
    
    @Query(value = "SELECT w FROM WordTestEntity w JOIN FETCH w.wordInfo",
            countQuery = "SELECT count(w) FROM WordTestEntity w")
    Page<WordTestEntity> findAllWithWordInfo(Pageable pageable);

    Page<WordTestEntity> findByUserInfo_UserUuid(String userUuid, Pageable pageable);}
