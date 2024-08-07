package com.smart.demo.controller;

import com.smart.demo.dto.UserDto;
import com.smart.demo.entity.LoginLogEntity;
import com.smart.demo.entity.UserEntity;
import com.smart.demo.entity.WordTestEntity;
import com.smart.demo.repository.UserRepository;
import com.smart.demo.repository.WordTestRepository;
import com.smart.demo.service.LoginLogService;
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
    private LoginLogService loginLogService;

    @Autowired
    private UserRepository userRepository;

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
    public String processLogin(@ModelAttribute UserDto userDto, HttpSession session, HttpServletResponse response, Model model) throws IOException {
        UserDto userResult = userService.login(userDto, session);

        UserEntity user = userRepository.findByUserEmail(userDto.getUserEmail())
                .orElse(null);

        UserEntity userPassword = userRepository.findByUserPassword(userDto.getUserPassword())
                .orElse(null);

        if (user == null || userPassword != null || user.getUserAble() == 0) {
            model.addAttribute("loginError", "아이디 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요.");
            return "App/login"; // 로그인 실패 후 이동할 페이지
        }

        if (userResult != null) {
            session.setAttribute("nickname", userResult.getUserNickname());
            session.setAttribute("email", userResult.getUserEmail());
            session.setAttribute("idx", userResult.getIdx());
            session.setAttribute("userUuid", userResult.getUserUuid());
            session.setAttribute("userAble", user.getUserAble()); // userAble 값을 세션에 저장

            // 로그인 로그 기록
            LoginLogEntity loginLog = loginLogService.logLogin(userResult.getIdx());
            session.setAttribute("loginLogId", loginLog.getIdx()); // 로그인 로그 ID를 세션에 저장

            return "App/main2"; // 로그인 성공 후 이동할 페이지
        } else {
            model.addAttribute("loginError", "아이디 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요.");

            return "App/login"; // 로그인 실패 후 이동할 페이지
        }
    }

    @GetMapping("/App/logout")
    public String logout(HttpSession session) {
        loginLogService.logLogout(session);
        session.invalidate();
        return "App/main1";
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
        userService.disableUser(idx);
        return "/App/main1";
    }

    @PostMapping("/user/nick-check")
    public @ResponseBody String nickCheck(@RequestParam("userNickname") String userNickname) {
        return userService.nickCheck(userNickname);
    }

    @PostMapping("/user/email-check")
    public @ResponseBody String emailCheck(@RequestParam("userEmail") String userEmail) {
        return userService.emailCheck(userEmail);
    }

    @GetMapping("/rankings")
    public String showRankings(Model model, HttpSession session) {
        // WordTestEntity 리스트 가져오기
        List<WordTestEntity> rankings = wordTestRepository.findAll();

        // 세션에서 userAble 값을 가져오기
        Integer userAble = (Integer) session.getAttribute("userAble");
        model.addAttribute("userAble", userAble);

        System.out.println("userAble = " + userAble);

        String nickname = (String) session.getAttribute("nickname");
        model.addAttribute("nickname", nickname);

        // 사용자별 최고 점수를 담을 맵
        Map<String, Map<Integer, WordTestEntity>> userBestScores = new HashMap<>();

        for (WordTestEntity entity : rankings) {
            if (entity.getUserInfo() != null) {  // Null 체크 추가
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
            } else {
                // getUserInfo()가 null인 경우 로그를 찍거나 처리하는 코드 추가
                System.out.println("Warning: WordTestEntity has null UserInfo.");
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

        model.addAttribute("user", session);
        model.addAttribute("rankingsByLevel", rankingsByLevel);

        return "App/rankings"; // 랭킹 페이지로 이동
    }

    @PostMapping("/find_pw")
    public String findPw(@RequestParam String userEmail, @RequestParam String userName, @RequestParam String userPhone, HttpSession session) {
        Optional<UserEntity> userOptional = userRepository.findByUserEmailAndUserNameAndUserPhone(userEmail, userName, userPhone);
        if (userOptional.isPresent()) {
            session.setAttribute("userEmail", userEmail); // 세션에 사용자 이메일 저장
            return "redirect:App/reset_pw";
        } else {
            return "redirect:App/find_pw?error"; // 에러 페이지로 리다이렉트
        }
    }

    @PostMapping("/reset_pw")
    public String resetPassword(@RequestParam("userPassword") String newPassword, HttpSession session, HttpServletResponse response) throws IOException {
        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail != null) {
            userService.resetPassword(userEmail, newPassword);
            session.removeAttribute("userEmail");
            return "redirect:/App/login";
        } else {
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html; charset=utf-8");
            out.println("<script> alert('Invalid session. Please try again.');");
            out.println("history.go(-1); </script>");
            out.close();

            return "App/reset_pw"; // 세션이 유효하지 않을 경우 재설정 페이지로 이동
        }
    }

    @GetMapping("/App/mypage-reset_pw/{idx}")
    public String mypage_reset_pwForm(@PathVariable Integer idx, Model model) {
        // 세션에서 userIdx를 가져와서 UserDto 객체를 조회
        UserDto userDto = userService.findById(idx);
        if (userDto != null) {
            model.addAttribute("user", userDto);
        } else {
            return "redirect:/login"; // 로그인 페이지로 리디렉션
        }
        return "/App/mypage-reset_pw";
    }

    @PostMapping("/reset_pw/{idx}") // 기존 URL 패턴 유지
    public String resetPassword(
            @RequestParam("userPassword") String newPassword,
            @RequestParam("userEmail") String userEmail,
            @PathVariable Integer idx,
            HttpSession session,
            HttpServletResponse response
    ) throws IOException {
        if (userEmail != null) {
            userService.resetPassword(userEmail, newPassword);
            session.removeAttribute("userEmail");
            return "redirect:/App/MyPage/" + idx; // 성공 후 리다이렉트
        } else {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('Invalid session. Please try again.');");
            out.println("window.location.href='/App/mypage-reset_pw?userEmail=" + userEmail + "';");
            out.println("</script>");
            out.close();
            return null; // 이 라인은 실제로는 도달하지 않음
        }
    }

    @PostMapping("/check_password")
    @ResponseBody
    public Map<String, Boolean> checkPassword(@RequestBody Map<String, String> request, HttpSession session) {
        String userEmail = (String) session.getAttribute("email");
        String inputPassword = request.get("password");

        Map<String, Boolean> response = new HashMap<>();
        boolean valid = userService.checkPassword(userEmail, inputPassword);
        response.put("valid", valid);

        return response;
    }
}