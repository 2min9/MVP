package com.smart.demo.controller;

import com.smart.demo.dto.UserDto;
import com.smart.demo.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/App/usersave")
    public String usersaveForm() {
        return "App/usersave";
    }

    @PostMapping("/App/usersave")
    public String usersave(@ModelAttribute UserDto userDto) {
        userService.save(userDto);
        return "App/login";
    }

    @PostMapping("/App/login")
    public String login(@ModelAttribute UserDto userDto, HttpSession session, HttpServletResponse response) throws IOException {
        UserDto userResult = userService.login(userDto);
        if(userResult != null) {
            session.setAttribute("loginEmail", userResult.getEmail());

            return "App/main2";
        } else {
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html; charset=utf-8");
            out.println("<script> alert('The ID or password is incorrect.');");
            out.println("history.go(-1); </script>");
            out.close();

            return "App/login";
        }
    }

    @GetMapping("/App/main2")
    public String main() {
        return "App/main2";
    }
}
