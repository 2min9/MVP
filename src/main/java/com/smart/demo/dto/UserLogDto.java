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
    private String login_ip;
    private String login_userAgent;
    private LocalDateTime login_in_Time;
    private LocalDateTime login_out_Time;
}
