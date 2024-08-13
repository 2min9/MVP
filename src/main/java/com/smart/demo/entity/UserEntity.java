package com.smart.demo.entity;

import com.smart.demo.dto.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Entity
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(nullable = false, length = 30)
    private String userName;

    @Column(nullable = false, length = 30)
    private String userNickname;

    @Column(nullable = false, length = 40)
    private String userEmail;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false, length = 10)
    private String userBirth;

    @Column(nullable = false, length = 15)
    private String userPhone;

    @Column(nullable = false, length = 1)
    private char userGender;

    @Column(nullable = false, length = 1)
    private Integer userAble;

    @Column(nullable = false, unique = true)
    private String userUuid;

    public static UserEntity toUserEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userDto.getUserName());
        userEntity.setUserNickname(userDto.getUserNickname());
        userEntity.setUserEmail(userDto.getUserEmail());
        userEntity.setUserPassword(userDto.getUserPassword());
        userEntity.setUserBirth(userDto.getUserBirth());

        // 전화번호에서 숫자만 추출
        String cleanedPhoneNumber = userDto.getUserPhone().replaceAll("[^0-9]", "");
        userEntity.setUserPhone(cleanedPhoneNumber);

        userEntity.setUserGender(userDto.getUserGender());
        userEntity.setUserAble(1);
        userEntity.setUserUuid(UUID.randomUUID().toString());
        return userEntity;
    }
}
