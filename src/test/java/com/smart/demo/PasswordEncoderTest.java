package com.smart.demo;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {

    @Test
    public void testPasswordUpdate() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String oldPassword = "oldpassword";
        String newPassword = "newpassword";

        // 1. 기존 비밀번호 암호화
        String encodedOldPassword = passwordEncoder.encode(oldPassword);
        System.out.println("Old Raw Password: " + oldPassword);
        System.out.println("Encoded Old Password: " + encodedOldPassword);

        // 2. 기존 비밀번호와 암호화된 비밀번호 비교
        boolean matchesOldPassword = passwordEncoder.matches(oldPassword, encodedOldPassword);
        System.out.println("Old Password Matches Encoded Old Password: " + matchesOldPassword);
        assert matchesOldPassword; // Ensure that the old password matches the encoded value

        // 3. 새 비밀번호 암호화
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        System.out.println("New Raw Password: " + newPassword);
        System.out.println("Encoded New Password: " + encodedNewPassword);

        // 4. 기존 암호화된 비밀번호를 새 비밀번호로 업데이트 (실제로는 DB에서 업데이트됨)
        // 이 예제에서는 업데이트된 암호화된 비밀번호를 시뮬레이션합니다
        String updatedEncodedPassword = encodedNewPassword;

        // 5. 새 비밀번호와 업데이트된 암호화된 비밀번호 비교
        boolean matchesNewPassword = passwordEncoder.matches(newPassword, updatedEncodedPassword);
        System.out.println("New Password Matches Updated Encoded Password: " + matchesNewPassword);
        assert matchesNewPassword; // Ensure that the new password matches the updated encoded value
    }
}
