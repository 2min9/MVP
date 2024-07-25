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

    @Column
    private String wordName;

    @Column
    private Integer wordLevel;

    @Column
    private String wordMean;

    @Column
    private String wordDetail;

    @Column
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
