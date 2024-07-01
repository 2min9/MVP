package com.smart.demo.repository;

import com.smart.demo.entity.WordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRepository extends JpaRepository<WordEntity, Integer> {
    List<WordEntity> findAll();

}
