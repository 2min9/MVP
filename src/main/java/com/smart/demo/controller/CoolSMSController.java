package com.smart.demo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

@RequiredArgsConstructor
@Controller
public class CoolSMSController {

    private final DefaultMessageService messageService;

    public CoolSMSController() {
        // 반드시 계정 내 등록된 유효한 API 키, API Secret Key를 입력해주셔야 합니다!
        this.messageService = NurigoApp.INSTANCE.initialize("NCSH8O1XOOPTETAM", "CN74TSTZB9VDXMAUFE1E1HY5U0VP6AWA", "https://api.coolsms.co.kr");
    }

    @GetMapping("/verification")
    public String verificationForm() {
        return "App/verificationForm";
    }

    @PostMapping("/send-code")
    @ResponseBody
    public String sendCode(@RequestParam("phone") String phone, HttpSession session) {
        String verificationCode = generateVerificationCode();
        session.setAttribute("verificationCode", verificationCode);

        Message message = new Message();
        message.setFrom("010-7197-1782"); // CoolSMS에 등록된 발신번호를 여기에 입력합니다.
        message.setTo(phone);
        message.setText("Your verification code is: " + verificationCode);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        if ("2000".equals(response.getStatusCode())) {
            return "success";
        } else {
            return "fail";
        }
    }

    @PostMapping("/verify-code")
    @ResponseBody
    public String verifyCode(@RequestParam("code") String code, HttpSession session) {
        String sentCode = (String) session.getAttribute("verificationCode");

        if (sentCode != null && sentCode.equals(code)) {
            return "success";
        } else {
            return "fail";
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000; // 100000 ~ 999999
        return String.valueOf(code);
    }
}