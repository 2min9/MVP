package com.smart.demo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLogDto {
    private Long idx;
    private String sessionId;
    private String connectionUrl;
    private String method;
    private LocalDateTime connectionTime;
}
