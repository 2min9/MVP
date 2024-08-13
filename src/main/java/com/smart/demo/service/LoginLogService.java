package com.smart.demo.service;

import com.smart.demo.entity.LoginLogEntity;
import com.smart.demo.repository.LoginLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class LoginLogService {

    @Autowired
    private LoginLogRepository loginLogRepository;

    public LoginLogEntity logLogin(Integer userId) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // IP 주소 처리
        String loginIp = request.getHeader("X-Forwarded-For");
        if (loginIp == null || loginIp.isEmpty()) {
            loginIp = request.getRemoteAddr(); // 기본 IP 주소
        }

        // User-Agent 처리
        String userAgent = request.getHeader("User-Agent");

        LoginLogEntity loginLog = new LoginLogEntity();
        loginLog.setLoginIp(loginIp);
        loginLog.setLoginUserAgent(userAgent);
        loginLog.setLoginInTime(Timestamp.valueOf(LocalDateTime.now()));

        return loginLogRepository.save(loginLog);
    }
}


