package com.smart.demo.entity;

import com.smart.demo.dto.WordDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "words_t")
public class WordEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column
    private String words_name;

    @Column
    private Integer words_difficulty;

    @Column
    private String words_mean;

    @Column
    private String words_similar;

    @Column
    private String words_pronunciation;

    public static WordEntity toSaveEntity(WordDto wordDto) {
        WordEntity wordEntity = new WordEntity();
        wordEntity.setWords_name(wordDto.getWords_name());
        wordEntity.setWords_difficulty(wordDto.getWords_difficulty());
        wordEntity.setWords_mean(wordDto.getWords_mean());
        wordEntity.setWords_similar(wordDto.getWords_similar());
        wordEntity.setWords_pronunciation("발음");
        // 추후에 변환
        return wordEntity;
    }
    public static WordEntity toUpdateEntity(WordDto wordDto) {
        WordEntity wordEntity = new WordEntity();
        wordEntity.setIdx(wordDto.getIdx());
        wordEntity.setWords_name(wordDto.getWords_name());
        wordEntity.setWords_difficulty(wordDto.getWords_difficulty());
        wordEntity.setWords_mean(wordDto.getWords_mean());
        wordEntity.setWords_similar(wordDto.getWords_similar());
        wordEntity.setWords_pronunciation("발음");
        // 추후에 변환
        return wordEntity;
    }
}
