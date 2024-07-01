package com.smart.demo.dto;


import com.smart.demo.entity.WordEntity;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Transactional
@Builder
public class WordDto {
    private Integer idx;
    private String words_name;
    private int words_difficulty;
    private String words_mean;
    private String words_similar;
    private String words_pronunciation;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public WordDto(int idx, String words_name, int words_difficulty, String words_mean, String words_similar, String words_pronunciation) {
        this.idx = idx;
        this.words_name = words_name;
        this.words_difficulty = words_difficulty;
        this.words_mean = words_mean;
        this.words_similar = words_similar;
        this.words_pronunciation = words_pronunciation;
    }

    public static WordDto toWordDto(WordEntity wordEntity) {
        WordDto wordDto = new WordDto();
        wordDto.setIdx(wordEntity.getIdx());
        wordDto.setWords_name(wordEntity.getWords_name());
        wordDto.setWords_difficulty(wordEntity.getWords_difficulty());
        wordDto.setWords_mean(wordEntity.getWords_mean());
        wordDto.setWords_similar(wordEntity.getWords_similar());
        wordDto.setWords_pronunciation(wordEntity.getWords_pronunciation());
        wordDto.setCreatedTime(wordEntity.getCreatedTime());
        wordDto.setUpdatedTime(wordEntity.getUpdatedTime());
        return wordDto;
    }
}
