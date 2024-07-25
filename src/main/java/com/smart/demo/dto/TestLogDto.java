package com.smart.demo.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TestLogDto {
    private Long idx;
    private double testInCorrentPercent; // 시험 오답률
    private String testRemainTime; // 시험 남은시간
}
