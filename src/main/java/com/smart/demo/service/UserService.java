package com.smart.demo.service;

import com.smart.demo.dto.UserDto;
import com.smart.demo.entity.UserEntity;
import com.smart.demo.entity.WordTestEntity;
import com.smart.demo.repository.UserRepository;
import com.smart.demo.repository.WordTestRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // PasswordEncoder를 추가합니다.

    public void save(UserDto userDto) {
        UserEntity userEntity = UserEntity.toUserEntity(userDto);
        userRepository.save(userEntity);
    }

    public UserDto login(UserDto userDto, HttpSession session) {
        Optional<UserEntity> byUserEmail = userRepository.findByUserEmail(userDto.getUserEmail());
        if (byUserEmail.isPresent()) {
            UserEntity userEntity = byUserEmail.get();
            if (userEntity.getUserPassword().equals(userDto.getUserPassword())) {
                UserDto dto = UserDto.toUserDto(userEntity);
                session.setAttribute("userUuid", userEntity.getUserUuid());
                return dto;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public List<UserDto> findAll() {
        List<UserEntity> userEntityList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (UserEntity userEntity : userEntityList) {
            userDtoList.add(UserDto.toUserDto(userEntity));
        }
        return userDtoList;
    }

    public UserDto findById(Integer idx) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(idx);
        if (optionalUserEntity.isPresent()) {
            return UserDto.toUserDto(optionalUserEntity.get());
        } else {
            return null;
        }
    }

    public UserDto updateForm(String myEmail) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserEmail(myEmail);
        if (optionalUserEntity.isPresent()) {
            return UserDto.toUserDto(optionalUserEntity.get());
        } else {
            return null;
        }
    }

    @Transactional
    public UserDto update(UserDto userDto) {
        UserEntity existingUserEntity = userRepository.findById(userDto.getIdx())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUserEntity.setUserName(userDto.getUserName());
        existingUserEntity.setUserNickname(userDto.getUserNickname());
        existingUserEntity.setUserEmail(userDto.getUserEmail());
        existingUserEntity.setUserBirth(userDto.getUserBirth());
        existingUserEntity.setUserPhone(userDto.getUserPhone());
        existingUserEntity.setUserGender(userDto.getUserGender());
        existingUserEntity.setUserAble(userDto.getUserAble());
        existingUserEntity.setUserUuid(userDto.getUserUuid() != null ? userDto.getUserUuid() : existingUserEntity.getUserUuid());

        // 비밀번호가 제공된 경우 업데이트
        if (userDto.getUserPassword() != null && !userDto.getUserPassword().isEmpty()) {
            System.out.println("비밀번호 = " + userDto.getUserPassword());
            existingUserEntity.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
        }

        userRepository.save(existingUserEntity);
        return UserDto.toUserDto(existingUserEntity);
    }



    public void deleteById(Integer idx) {
        userRepository.deleteById(idx);
    }

    public String nickCheck(String nickname) {
        Optional<UserEntity> byUserID = userRepository.findByUserNickname(nickname);
        if (byUserID.isPresent()) {
            //조회결과가 없다면 --> 등록된 아이디가 있다. (사용할 수 없다)
            return null;
        } else {
            //조회 결과가 있다 --> 사용가능하다
            return "OK";
        }
    }
}
