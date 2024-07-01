package com.smart.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdmController {
    @GetMapping("/AdminPage")
    public String AdminPage() {
        return "Admin/AdminPage";
    }
    @GetMapping("/User")
    public String User() {
        return "Admin/User";
    }
    @GetMapping("/Word")
    public String Word() {
        return "Admin/Word";
    }
    @GetMapping("StatData-AgeLayer")
    public String StatData_AgeLayer() {
        return "Admin/StatData-AgeLayer";
    }
    @GetMapping("StatData-AnswerRate")
    public String StatData_AnswerRate() {
        return "Admin/StatData-AnswerRate";
    }
    @GetMapping("StatData-DailyContact")
    public String StatData_DailyContact() {
        return "Admin/StatData-DailyContact";
    }
    @GetMapping("StatData-Login")
    public String StatData_Login() {
        return "Admin/StatData-Login";
    }
    @GetMapping("LogData")
    public String LogData() {
        return "Admin/LogData";
    }
}
