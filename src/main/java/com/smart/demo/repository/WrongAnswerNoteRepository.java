package com.smart.demo.repository;import com.smart.demo.entity.WrongAnswerNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WrongAnswerNoteRepository extends JpaRepository<WrongAnswerNoteEntity, Integer> {
}
