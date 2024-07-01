package com.smart.demo.dto;

import com.smart.demo.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private Integer idx;
    private String name_t;
    private String nickname;
    private String email;
    private String password_t;
    private String birth_year_t;
    private String phone_t;
    private char gender;
    private char user_able;
    private LocalDateTime created_time;

    public static UserDto toUserDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setIdx(userEntity.getIdx());
        userDto.setName_t(userEntity.getName_t());
        userDto.setNickname(userEntity.getNickname());
        userDto.setEmail(userEntity.getEmail());
        userDto.setPassword_t(userEntity.getPassword_t());
        userDto.setBirth_year_t(userEntity.getBirth_year_t());
        userDto.setPhone_t(userEntity.getPhone_t());
        userDto.setGender(userEntity.getGender());
        userDto.setUser_able(userEntity.getUser_able());
        userDto.setCreated_time(userEntity.getCreatedTime());

        return userDto;
    }
}
