package com.smart.demo.dto;

import com.smart.demo.entity.WrongAnswerNoteEntity;
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
public class WrongAnswerNoteDto {
    private Integer idx;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public static WrongAnswerNoteDto wrongAnswerNoteDto(WrongAnswerNoteEntity wrongAnswerNoteEntity) {
        WrongAnswerNoteDto wrongAnswerNoteDto = new WrongAnswerNoteDto();
        wrongAnswerNoteDto.setIdx(wrongAnswerNoteEntity.getIdx());
        wrongAnswerNoteDto.setCreatedTime(wrongAnswerNoteEntity.getCreatedTime());
        wrongAnswerNoteDto.setUpdatedTime(wrongAnswerNoteEntity.getUpdatedTime());

        return wrongAnswerNoteDto;
    }
}
