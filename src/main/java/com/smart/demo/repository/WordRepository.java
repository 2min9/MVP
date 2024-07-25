package com.smart.demo.repository;

import com.smart.demo.entity.WordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<WordEntity, Integer> {
    List<WordEntity> findAll();

    List<WordEntity> findByWordLevel(int wordLevel);

    WordEntity findByWordName(String wordName);
}
