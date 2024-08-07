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

    @Column
    private String userName;

    @Column
    private String userNickname;

    @Column
    private String userEmail;

    @Column
    private String userPassword;

    @Column
    private String userBirth;

    @Column
    private String userPhone;

    @Column
    private char userGender;

    @Column
    private Integer userAble = 1;

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
