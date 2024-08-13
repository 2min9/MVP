package com.smart.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class WordTestEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(nullable = false, length = 1)
    private Integer level;

    @Column(length = 3)
    private Integer testPoint;

    @Column(length = 2)
    private Integer solvedCount;

    @Column(nullable = false, length = 1)
    private Integer language;

    @Column(length = 2)
    private Integer questionCount;

    @Column(nullable = false, length = 1)
    private Integer testAble;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "word_info_idx", referencedColumnName = "idx")
    private WordEntity wordInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_info_idx", referencedColumnName = "userUuid")
    private UserEntity userInfo;

    @OneToMany(mappedBy = "wordTestEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TestResultEntity> testResults = new ArrayList<>();

}