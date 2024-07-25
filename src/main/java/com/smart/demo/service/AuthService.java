package com.smart.demo.service;

import com.smart.demo.dto.UserDto;
import com.smart.demo.entity.UserEntity;
import com.smart.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserEmail())
                .password(user.getUserPassword())
                .roles("USER")
                .build();
    }

    public UserDto login(UserDto userDto, HttpSession session) {
        String userEmail = userDto.getUserEmail();
        String password = userDto.getUserPassword();

        UserEntity user = userRepository.findByUserEmail(userEmail)
                .orElse(null);

        if (user != null) {
            String encodedPassword = user.getUserPassword();
            System.out.println("Stored encoded password: " + encodedPassword);
            System.out.println("Input password: " + password);
            System.out.println("Password matches: " + passwordEncoder.matches(password, encodedPassword));

            if (passwordEncoder.matches(password, encodedPassword)) {
                session.setAttribute("userUuid", user.getUserUuid());
                return UserDto.toUserDto(user);
            }
        }

        return null;
    }
}
