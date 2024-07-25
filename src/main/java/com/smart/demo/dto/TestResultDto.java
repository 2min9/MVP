package com.smart.demo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TestResultDto {
    private Integer idx;
    private String testNum;
    private String answer;
    private String ox;
    private LocalDateTime createdTime;
}
