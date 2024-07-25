package com.smart.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "App/main1";
    }

    @GetMapping("/main2")
    public String main2() {
        return "App/main2";
    }

    @GetMapping("/test")
    public String test() {
        return "App/test";
    }

    @GetMapping("/id_search")
    public String id_search() {
        return "App/id_search";
    }
}
