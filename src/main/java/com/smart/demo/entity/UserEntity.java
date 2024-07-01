package com.smart.demo.entity;

import com.smart.demo.dto.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class UserEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column
    private String name_t;

    @Column
    private String nickname;

    @Column
    private String email;

    @Column
    private String password_t;

    @Column
    private String birth_year_t;

    @Column
    private String phone_t;

    @Column
    private char gender;

    @Column
    private char user_able;


    public static UserEntity toUserEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName_t(userDto.getName_t());
        userEntity.setNickname(userDto.getNickname());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword_t(userDto.getPassword_t());
        userEntity.setBirth_year_t(userDto.getBirth_year_t());
        userEntity.setPhone_t(userDto.getPhone_t());
        userEntity.setGender(userDto.getGender());
        userEntity.setUser_able(userDto.getUser_able());

        return userEntity;
    }

    public static UserEntity toUpdateUserEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setIdx(userDto.getIdx());
        userEntity.setName_t(userDto.getName_t());
        userEntity.setNickname(userDto.getNickname());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword_t(userDto.getPassword_t());
        userEntity.setBirth_year_t(userDto.getBirth_year_t());
        userEntity.setPhone_t(userDto.getPhone_t());
        userEntity.setGender(userDto.getGender());
        userEntity.setUser_able(userDto.getUser_able());

        return userEntity;
    }
}
