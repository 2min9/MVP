package com.smart.demo.controller;

import com.smart.demo.dto.WordDto;
import com.smart.demo.service.WordService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuizKoreaController {

    @Autowired
    private WordService wordService;

    @GetMapping("/App/MemorizeSelectLevelKorea")
    public String MemorizeSelectLevelKorea() {
        return "App/MemorizeSelectLevelKorea";
    }

    @GetMapping("/App/ResultSelectLevelKorea")
    public String ResultSelectLevelKorea() {
        return "App/ResultSelectLevelKorea";
    }

    @GetMapping("/App/Quiz/MemorizeKorea/{wordLevel}")
    public String MemorizeKoreaByLevel(@PathVariable int wordLevel, Model model, HttpSession session) {
        List<WordDto> wordList = wordService.findByWordLevel(wordLevel);

        if (wordList.isEmpty()) {
            model.addAttribute("message", "해당 단계에 단어가 없습니다.");
            return "App/WordQuizResult"; // 빈 화면 또는 적절한 페이지로 리다이렉트
        }

        session.setAttribute("wordList", wordList);
        session.setAttribute("solvedWords", new ArrayList<Integer>());
        session.setAttribute("solvedCount", 0);
        session.setAttribute("wordLevel", wordLevel);

        return "redirect:/App/Quiz/MemorizeKorea"; // 랜덤 퀴즈 페이지로 이동
    }

    @GetMapping("/App/Quiz/TestKorea/{wordLevel}")
    public String TestKoreaByLevel(@PathVariable int wordLevel, Model model, HttpSession session) {
        List<WordDto> wordList = wordService.findByWordLevel(wordLevel);

        if (wordList.isEmpty()) {
            model.addAttribute("message", "해당 단계에 단어가 없습니다.");
            return "App/WordQuizResult"; // 빈 화면 또는 적절한 페이지로 리다이렉트
        }

        session.setAttribute("wordList", wordList);
        session.setAttribute("solvedWords", new ArrayList<Integer>());
        session.setAttribute("solvedCount", 0);
        session.setAttribute("wordLevel", wordLevel);

        return "redirect:/App/Quiz/RandomKorea"; // 랜덤 퀴즈 페이지로 이동
    }

    @GetMapping("/App/Quiz/MemorizeKorea")
    public String randomQuizMemorizeKorea(Model model, HttpSession session) {
        List<Integer> solvedWords = (List<Integer>) session.getAttribute("solvedWords");
        List<WordDto> wordList = (List<WordDto>) session.getAttribute("wordList");
        String examMode = (String) session.getAttribute("examMode");

        // 풀었던 단어 목록을 초기화하거나, 최초 접근인 경우에는 생성
        if (solvedWords == null) {
            solvedWords = new ArrayList<>();
        }

        WordDto randomWord = wordService.findRandomWordFromList(wordList, solvedWords, examMode);
        if (randomWord != null) {
            model.addAttribute("word", randomWord);
            model.addAttribute("quizMode", true); // 퀴즈 모드를 설정하여 화면에서 조건 처리할 수 있도록 함

            // 현재 풀고 있는 문제 번호 계산
            int currentQuestionNumber = solvedWords.size() + 1;
            session.setAttribute("currentQuestionNumber", currentQuestionNumber);
            model.addAttribute("currentQuestionNumber", currentQuestionNumber);

            // 현재 단어를 풀었음을 세션에 저장
            solvedWords.add(randomWord.getIdx());
            session.setAttribute("solvedWords", solvedWords);

            return "App/WordQuizMemorizeKorea"; // 단어 맞추기 및 결과 페이지로 이동
        } else {
            // 단어가 없을 경우 처리
            return "redirect:/Admin/Word"; // 리스트 페이지로 이동
        }
    }

    @PostMapping("/checkAnswerMemorizeKorea")
    public String checkAnswerMemorizeKorea(@RequestParam Integer wordIdx, @RequestParam String userAnswer, Model model, HttpSession session) {
        // 단어 ID를 사용하여 정답을 가져옵니다.
        WordDto wordDto = wordService.findById(wordIdx);

        if (wordDto != null) {
            String[] names = wordDto.getWordName().split(", ");
            boolean isCorrect = Arrays.stream(names)
                    .anyMatch(name -> name.equalsIgnoreCase(userAnswer));
            if (isCorrect) {
                model.addAttribute("resultMessage", "정답입니다!");
            } else {
                model.addAttribute("resultMessage2", "틀렸습니다. 정답은 " + wordDto.getWordMean() + "입니다.");
            }
        } else {
            model.addAttribute("resultMessage", "단어를 찾을 수 없습니다.");
        }

        // 퀴즈 모드를 유지하고 답 검증 결과를 표시합니다.
        model.addAttribute("word", wordDto);
        model.addAttribute("quizMode", true);

        return "App/WordQuizMemorizeKorea";
    }

    @GetMapping("/App/Quiz/NextMemorizeKorea")
    public String nextQuizMemorizeKorea(Model model, HttpSession session) {
        return randomQuizMemorizeKorea(model, session); // 기존 randomQuiz 메서드를 재사용
    }

    @GetMapping("/App/Quiz/RandomKorea")
    public String randomQuizKorea(Model model, HttpSession session) {
        List<Integer> solvedWords = (List<Integer>) session.getAttribute("solvedWords");
        List<WordDto> wordList = (List<WordDto>) session.getAttribute("wordList");
        String examMode = (String) session.getAttribute("examMode");
        // 풀었던 단어 목록을 초기화하거나, 최초 접근인 경우에는 생성
        if (solvedWords == null) {
            solvedWords = new ArrayList<>();
        }

        // 풀지 않은 단어가 30개 이상인 경우에는 solvedWords를 초기화
        if (solvedWords.size() >= 30) {
            solvedWords.clear();
        }

        WordDto randomWord = wordService.findRandomWordFromList(wordList, solvedWords, examMode);
        if (randomWord != null) {
            model.addAttribute("word", randomWord);
            model.addAttribute("quizMode", true); // 퀴즈 모드를 설정하여 화면에서 조건 처리할 수 있도록 함

            // 현재 단어를 풀었음을 세션에 저장
            solvedWords.add(randomWord.getIdx());
            session.setAttribute("solvedWords", solvedWords);

            int solvedCount = (int) session.getAttribute("solvedCount");
            solvedCount++;
            session.setAttribute("solvedCount", solvedCount);

            model.addAttribute("solvedCount", solvedCount);
            model.addAttribute("lastQuestion", solvedCount == 30);

            return "App/WordQuizResultKorea"; // 단어 맞추기 및 결과 페이지로 이동
        } else {
            // 단어가 없을 경우 처리
            return "redirect:/Admin/Word"; // 리스트 페이지로 이동
        }
    }

    // 새로운 퀴즈 문제를 불러오는 메서드 추가
    @GetMapping("/App/Quiz/NextKorea")
    public String nextQuizKorea(Model model, HttpSession session) {
        return randomQuizKorea(model, session); // 기존 randomQuiz 메서드를 재사용
    }

    // 제출 버튼 클릭 시 세션 초기화 처리
    @GetMapping("/App/Quiz/SubmitKorea")
    public String submitQuizKorea(HttpSession session) {
        session.removeAttribute("solvedWords"); // 풀었던 단어 목록을 초기화
        session.removeAttribute("currentQuestionNumber"); // 현재 문제 번호 초기화
        return "redirect:/main2"; // 메인 화면으로 이동
    }
}
