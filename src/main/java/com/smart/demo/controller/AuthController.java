package com.smart.demo.controller;

import com.smart.demo.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/guest-login")
    public RedirectView guestLogin(HttpServletRequest request) {
        // 기존 세션을 무효화하고 새 세션을 생성
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 기존 세션 무효화
        }
        session = request.getSession(true); // 새로운 세션 생성

        // 게스트 사용자 인증 설정
        UserDetails guestUser = userDetailsService.loadGuestUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(guestUser, null, guestUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 세션에 추가 데이터 설정 (예: session.idx)
        session.setAttribute("idx", 0); // 또는 다른 값
        // 게스트 세션 속성 설정
        session.setAttribute("guestUser", true);

        return new RedirectView("/main2"); // 로그인 후 홈 페이지로 리다이렉션
    }

    private String generateSessionIdx() {
        // 세션 인덱스 생성 로직 (예: UUID 사용)
        return java.util.UUID.randomUUID().toString();
    }
}
