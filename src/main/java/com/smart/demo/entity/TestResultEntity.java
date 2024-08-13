package com.smart.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "testresultentity")
public class TestResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(nullable = false)
    private Integer testNum;

    @Column(nullable = false, length = 20)
    private String answer;

    @Column(nullable = false, length = 1)
    private String ox;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "word_test_entity_idx")
    private WordTestEntity wordTestEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "word_idx")
    private WordEntity wordIdx;
}
