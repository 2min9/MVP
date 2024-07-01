package com.smart.demo.service;

import com.smart.demo.dto.UserDto;
import com.smart.demo.entity.UserEntity;
import com.smart.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(UserDto userDto) {
        UserEntity userEntity = UserEntity.toUserEntity(userDto);
        userRepository.save(userEntity);
    }

    public UserDto login(UserDto userDto) {
        Optional<UserEntity> byUserEmail = userRepository.findByEmail(userDto.getEmail());
        if(byUserEmail.isPresent()) {
            UserEntity userEntity = byUserEmail.get();
            if(userEntity.getPassword_t().equals(userDto.getPassword_t())) {
                UserDto dto = UserDto.toUserDto(userEntity);
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
        for(UserEntity userEntity: userEntityList) {
            userDtoList.add(UserDto.toUserDto(userEntity));
        }
        return userDtoList;
    }

    public UserDto findById(Integer idx) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(idx);
        if(optionalUserEntity.isPresent()) {
            return UserDto.toUserDto(optionalUserEntity.get());
        } else {
            return null;
        }
    }

    public UserDto updateForm(String myEmail) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(myEmail);
        if(optionalUserEntity.isPresent()) {
            return UserDto.toUserDto(optionalUserEntity.get());
        } else {
            return null;
        }
    }

    public void update(UserDto userDto) {
        userRepository.save(UserEntity.toUpdateUserEntity(userDto));
    }

    public void deleteById(Integer idx) {
        userRepository.deleteById(idx);
    }
}
