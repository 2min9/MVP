package com.smart.demo.repository;

import com.smart.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUserEmail(String userEmail);

    Optional<UserEntity> findByUserNickname(String userNickname);

    UserEntity findByUserUuid(String userUuid);

    UserEntity findByUserNameAndUserPhone(String userName, String userPhone);

    Optional<UserEntity> findByUserEmailAndUserNameAndUserPhone(String userEmail, String userName, String userPhone);
}
