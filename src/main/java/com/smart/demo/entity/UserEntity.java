package com.smart.demo.entity;

import com.smart.demo.dto.UserDto;
import com.smart.demo.security.PasswordConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class UserEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column
    private String userName;

    @Column
    private String userNickname;

    @Column
    private String userEmail;

    @Convert(converter = PasswordConverter.class)
    @Column
    private String userPassword; // PasswordConverter 적용

    @Column
    private String userBirth;

    @Column
    private String userPhone;

    @Column
    private char userGender;

    @Column
    private char userAble;

    @Column(nullable = false, unique = true)
    private String userUuid;

    public static UserEntity toUserEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userDto.getUserName());
        userEntity.setUserNickname(userDto.getUserNickname());
        userEntity.setUserEmail(userDto.getUserEmail());
        userEntity.setUserPassword(userDto.getUserPassword());
        userEntity.setUserBirth(userDto.getUserBirth());
        userEntity.setUserPhone(userDto.getUserPhone());
        userEntity.setUserGender(userDto.getUserGender());
        userEntity.setUserAble(userDto.getUserAble());
        userEntity.setUserUuid(UUID.randomUUID().toString());

        return userEntity;
    }

    public static UserEntity toUpdateUserEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setIdx(userDto.getIdx());
        userEntity.setUserName(userDto.getUserName());
        userEntity.setUserNickname(userDto.getUserNickname());
        userEntity.setUserEmail(userDto.getUserEmail());
        userEntity.setUserPassword(userDto.getUserPassword());
        userEntity.setUserBirth(userDto.getUserBirth());
        userEntity.setUserPhone(userDto.getUserPhone());
        userEntity.setUserGender(userDto.getUserGender());
        userEntity.setUserAble(userDto.getUserAble());
        userEntity.setUserUuid(UUID.randomUUID().toString());

        return userEntity;
    }
}
