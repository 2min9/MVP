package com.smart.demo.dto;


import com.smart.demo.entity.WordEntity;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
    private String wordName;
    private int wordLevel;
    private String wordMean;
    private String wordDetail;
    private char wordAble;
    private double word_incorrect_percent;
//    private Timestamp createdTime;
//    private Timestamp updatedTime;

    private String selectedWordName;

    public WordDto(Integer idx, String wordName, String wordMean) {
        this.idx = idx;
        this.wordName = wordName;
        this.wordMean = wordMean;
    }

    // WordEntity를 WordDto로 변환하는 메서드
    public static WordDto toWordDto(WordEntity wordEntity) {
        if (wordEntity == null) {
            return null;
        }
        return WordDto.builder()
                .idx(wordEntity.getIdx())
                .wordName(wordEntity.getWordName())
                .wordLevel(wordEntity.getWordLevel())
                .wordMean(wordEntity.getWordMean())
                .wordDetail(wordEntity.getWordDetail())
                .wordAble(wordEntity.getWordAble())
//                .createdTime(wordEntity.getCreatedTime())
//                .updatedTime(wordEntity.getUpdatedTime())
                .build();
    }

}
