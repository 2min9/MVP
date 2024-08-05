package com.smart.demo.repository;

import com.smart.demo.entity.TestResultEntity;
import com.smart.demo.entity.WordEntity;
import com.smart.demo.entity.WordTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResultEntity, Integer> {

    @Query("SELECT tr FROM TestResultEntity tr " +
            "JOIN FETCH tr.wordTestEntity wte " +
            "JOIN FETCH wte.wordInfo wi " +
            "WHERE wte.idx = :wordTestEntityId")
    List<TestResultEntity> findByWordTestEntityIdWithWordInfo(@Param("wordTestEntityId") Integer wordTestEntityId);

    TestResultEntity findByTestNumAndWordTestEntity(Integer idx, WordTestEntity wordTestEntity);

    List<TestResultEntity> findByWordTestEntityIdx(Integer wordTestEntityIdx);
}
