package com.smart.demo.controller;

import com.smart.demo.dto.UserDto;
import com.smart.demo.dto.WordDto;
import com.smart.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdmController {

    private final UserService userService;

    @GetMapping("/AdminPage")
    public String AdminPage() {
        return "Admin/AdminPage";
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
