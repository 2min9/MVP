package com.smart.demo.controller;

import com.smart.demo.entity.UserEntity;
import com.smart.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/find_id")
public class FindIdController {


    private final UserRepository userRepository;

    public FindIdController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public String findId(@RequestParam String userName, @RequestParam String userPhone) {
        UserEntity user = userRepository.findByUserNameAndUserPhone(userName, userPhone);
        if (user != null) {
            return user.getUserEmail();
        } else {
            return "a";
        }
    }
}
