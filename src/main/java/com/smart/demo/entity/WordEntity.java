package com.smart.demo.entity;

import com.smart.demo.dto.WordDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "wordinfo")
public class WordEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(nullable = false, length = 30)
    private String wordName;

    @Column(nullable = false, length = 1)
    private Integer wordLevel;

    @Column(nullable = false, length = 100)
    private String wordMean;

    @Column(nullable = false)
    private String wordDetail;

    @Column(nullable = false, length = 1)
    private char wordAble;

    @OneToMany(mappedBy = "wordIdx", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestResultEntity> testResults = new ArrayList<>();

    public static WordEntity toSaveEntity(WordDto wordDto) {
        WordEntity wordEntity = new WordEntity();
        wordEntity.setWordName(wordDto.getWordName());
        wordEntity.setWordLevel(wordDto.getWordLevel());
        wordEntity.setWordMean(wordDto.getWordMean());
        wordEntity.setWordDetail(wordDto.getWordDetail());
        // 추후에 변환
        return wordEntity;
    }
}
