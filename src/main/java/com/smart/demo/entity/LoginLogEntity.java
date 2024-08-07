package com.smart.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "login_log")
public class LoginLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(nullable = false)
    private String loginIp;

    @Column(nullable = false)
    private String loginUserAgent;

    @Column(nullable = false)
    private LocalDateTime loginInTime;

    private LocalDateTime loginOutTime; // 맞춤법 수정
}
