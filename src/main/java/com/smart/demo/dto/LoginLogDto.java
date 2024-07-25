package com.smart.demo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginLogDto {
    private Long idx;
    private String loginIp; // 사용자의 IP
    private String loginUserAgent; //사용자의 웹 브라우저와, 기기의 정보를 담고 있다.
    private LocalDateTime loginInTime; // 접속 했을 때 시간
    private LocalDateTime loginOutDate; // 접속 종료 했을 때 시간
}
