package com.smart.demo.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 실제 사용자 정보를 반환하지 않고, 모든 요청에 대해 게스트 사용자로 처리
        return User.builder()
                .username("guest")
                .password("") // 비밀번호는 비워두거나, 빈 문자열 사용
                .roles("GUEST")
                .build();
    }

    public UserDetails loadGuestUser() {
        // 실제로는 비밀번호를 설정하지 않거나, 매우 단순한 비밀번호를 사용합니다.
        return User.builder()
                .username("guest")
                .password("") // 비밀번호는 비워두거나, 빈 문자열 사용
                .roles("GUEST")
                .build();
    }
}
