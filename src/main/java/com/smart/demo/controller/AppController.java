package com.smart.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
    @GetMapping("/App/login")
    public String login() {
        return "App/login";
    }

    @GetMapping("/App/kakaologin")
    public String kakao() { return "App/kakaologin";}
}
