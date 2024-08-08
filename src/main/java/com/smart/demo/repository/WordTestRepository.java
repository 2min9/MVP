package com.smart.demo.repository;

import com.smart.demo.entity.WordTestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordTestRepository extends JpaRepository<WordTestEntity, Integer> {

    Page<WordTestEntity> findByUserInfo_UserUuidAndTestAble(String userUuid, int testAble, Pageable pageable);
}
