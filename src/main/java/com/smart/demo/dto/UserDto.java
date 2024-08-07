package com.smart.demo.dto;

import com.smart.demo.entity.UserEntity;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.websocket.OnMessage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private Integer idx;

    @NotEmpty(message = "이름은 필수 항목 입니다.")
    private String userName;

    @NotEmpty(message = "별명은 필수 항목 입니다.")
    private String userNickname;

    @NotEmpty(message = "이메일은 필수 항목 입니다.")
    @Email
    private String userEmail;

    @Size(min=1, max = 20)
    private String userPassword;

    @NotEmpty(message = "생년월일은 필수 항목 입니다.")
    private String userBirth;

    @NotEmpty(message = "전화번호는 필수 항목 입니다.")
    private String userPhone;

    @NotEmpty(message = "성별은 필수 항목 입니다.")
    private char userGender;

    private Integer userAble;

    private String userUuid;

    private LocalDateTime created_time;

    public static UserDto toUserDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setIdx(userEntity.getIdx());
        userDto.setUserName(userEntity.getUserName());
        userDto.setUserNickname(userEntity.getUserNickname());
        userDto.setUserEmail(userEntity.getUserEmail());
        userDto.setUserPassword(userEntity.getUserPassword());
        userDto.setUserBirth(userEntity.getUserBirth());
        userDto.setUserPhone(userEntity.getUserPhone());
        userDto.setUserGender(userEntity.getUserGender());
        userDto.setUserAble(userEntity.getUserAble());
        userDto.setUserUuid(userEntity.getUserUuid());
        userDto.setCreated_time(userEntity.getCreatedTime());
        return userDto;
    }
}
