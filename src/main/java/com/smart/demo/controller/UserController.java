package com.smart.demo.controller;

import com.smart.demo.dto.UserDto;
import com.smart.demo.entity.UserEntity;
import com.smart.demo.entity.WordTestEntity;
import com.smart.demo.repository.WordTestRepository;
import com.smart.demo.service.AuthService;
import com.smart.demo.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final WordTestRepository wordTestRepository;

    @Autowired
    private AuthService authService;

    @GetMapping("/App/signup")
    public String signupForm() {
        return "App/signup";
    }

    @PostMapping("/App/signup")
    public String signup(@ModelAttribute UserDto userDto) {
        userService.save(userDto);
        return "redirect:/App/login";
    }

    @GetMapping("/App/find_id")
    public String find_idForm() {
        return "App/find_id";
    }

    @GetMapping("/App/find_pw")
    public String find_pwForm() {
        return "App/find_pw";
    }

    @GetMapping("/App/reset_pw")
    public String reset_pwForm() {
        return "App/reset_pw";
    }

    @GetMapping("App/login")
    public String login() {
        return "App/login";
    }

    @PostMapping("App/login")
    public String processLogin(@ModelAttribute UserDto userDto, HttpSession session, HttpServletResponse response) throws IOException {
        UserDto userResult = authService.login(userDto, session);

        if (userResult != null) {
            session.setAttribute("email", userResult.getUserEmail());
            session.setAttribute("idx", userResult.getIdx());
            session.setAttribute("userUuid", userResult.getUserUuid()); // userUuid 추가
            return "App/main2"; // 로그인 성공 후 이동할 페이지
        } else {
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html; charset=utf-8");
            out.println("<script> alert('Invalid ID or password.');");
            out.println("history.go(-1); </script>");
            out.close();

            return "App/login"; // 로그인 실패 후 이동할 페이지
        }
    }

    @GetMapping("/App/id_search")
    public String id_search() {
        return "App/id_search";
    }

    @GetMapping("/App/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "App/main2";
    }

    @GetMapping("/App/MyPage/{idx}")
    public String myPage(@PathVariable Integer idx, Model model) {
        List<UserDto> userDtoList = userService.findAll();
        UserDto userDto = userService.findById(idx);
        model.addAttribute("userList", userDtoList);
        model.addAttribute("user", userDto);
        return "App/MyPage";
    }

    @GetMapping("/App/MyPage/update/{idx}")
    public String myPageUpdateForm(@PathVariable Integer idx, Model model) {
        UserDto userDto = userService.findById(idx);
        model.addAttribute("user", userDto);
        return "App/MyPageUpdate";
    }

    @PostMapping("/App/MyPage/update/{idx}")
    public String myPageUpdate(@ModelAttribute UserDto userDto, Model model, @PathVariable Integer idx) {
        UserDto user = userService.update(userDto);
        model.addAttribute("user", user);
        return "redirect:/App/MyPage/" + idx;
    }


    @GetMapping("/User")
    public String User(Model model) {
        List<UserDto> userDtoList = userService.findAll();
        model.addAttribute("userList", userDtoList);
        return "Admin/User";
    }

    @GetMapping("/user/update/{idx}")
    public String UserUpdateForm(@PathVariable Integer idx, Model model) {
        UserDto userDto = userService.findById(idx);
        model.addAttribute("user", userDto);
        return "Admin/UserUpdate";
    }

    @PostMapping("/user/update/{idx}")
    public String UserUpdate(@ModelAttribute UserDto userDto, Model model) {
        UserDto user = userService.update(userDto);
        model.addAttribute("user", user);
        return "redirect:/User";
    }

    @GetMapping("/user/delete/{idx}")
    public String delete(@PathVariable Integer idx) {
        userService.deleteById(idx);
        return "redirect:/User";
    }

    @PostMapping("/user/nick-check")
    public @ResponseBody String nickCheck(@RequestParam("userNickname") String userNickname) {
        return userService.nickCheck(userNickname);
    }

    @GetMapping("/rankings")
    public String showRankings(Model model) {
        // WordTestEntity 리스트 가져오기
        List<WordTestEntity> rankings = wordTestRepository.findAll();

        // 사용자별 최고 점수를 담을 맵
        Map<String, Map<Integer, WordTestEntity>> userBestScores = new HashMap<>();

        for (WordTestEntity entity : rankings) {
            String userEmail = entity.getUserInfo().getUserEmail();
            int level = entity.getLevel();

            // 사용자의 해당 레벨에서의 최고 점수를 맵에 저장
            userBestScores.putIfAbsent(userEmail, new HashMap<>());
            Map<Integer, WordTestEntity> userScores = userBestScores.get(userEmail);

            // Null 검사 후 최고 점수를 업데이트
            WordTestEntity currentBest = userScores.get(level);
            if (currentBest == null || entity.getTestPoint() > currentBest.getTestPoint()) {
                userScores.put(level, entity);
            }
        }

        // 레벨별로 데이터 필터링 및 정렬
        List<List<WordTestEntity>> rankingsByLevel = new ArrayList<>();
        for (int i = 1; i <= 5; i++) { // 5개 레벨에 대해 반복
            final int level = i;
            List<WordTestEntity> levelRankings = userBestScores.values().stream()
                    .map(scores -> scores.get(level))
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(WordTestEntity::getTestPoint).reversed())
                    .collect(Collectors.toList());
            rankingsByLevel.add(levelRankings);
        }

        model.addAttribute("rankingsByLevel", rankingsByLevel);

        return "App/rankings"; // 랭킹 페이지로 이동
    }



}
