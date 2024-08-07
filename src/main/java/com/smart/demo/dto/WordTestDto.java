package com.smart.demo.dto;

import com.smart.demo.entity.UserEntity;
import com.smart.demo.entity.WordEntity;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Transactional
@Builder
public class WordTestDto {
    private Integer idx;
    private Integer testPoint;
    private Integer level;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private UserEntity userInfo; // UserEntity 참조
    private List<TestResultDto> results;


    @Getter
    @Setter
    public static class TestResultDto {
        private String testNum;
        private String answer;
        private String ox;
        private WordEntity wordInfo; // WordEntity 참조
    }
}
