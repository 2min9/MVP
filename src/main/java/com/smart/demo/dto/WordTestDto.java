package com.smart.demo.dto;

import com.smart.demo.entity.WordTestEntity;
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
public class WordTestDto {
    private int idx;
    private int text_t_point;
    private double test_t_incorrect_percent;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public static WordTestDto toWordTestDto(WordTestEntity wordTestEntity) {
        WordTestDto wordTestDto = new WordTestDto();
        wordTestDto.setIdx(wordTestEntity.getIdx());
        wordTestDto.setText_t_point(wordTestEntity.getTest_point());
        wordTestDto.setTest_t_incorrect_percent(wordTestEntity.getTest_incorrect_percent());
        wordTestDto.setCreatedTime(wordTestEntity.getCreatedTime());
        wordTestDto.setUpdatedTime(wordTestEntity.getUpdatedTime());

        return wordTestDto;
    }
}
