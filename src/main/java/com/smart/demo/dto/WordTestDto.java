package com.smart.demo.dto;

import com.smart.demo.entity.UserEntity;
import com.smart.demo.entity.WordEntity;
import com.smart.demo.entity.WordTestEntity;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private UserEntity userInfo; // UserEntity 참조
    private List<TestResultDto> results;
    private Integer testAble;

    @Getter
    @Setter
    public static class TestResultDto {
        private String testNum;
        private String answer;
        private String ox;
        private WordEntity wordInfo; // WordEntity 참조
    }
}
