package com.smart.demo.controller;

import com.smart.demo.dto.UserDto;
import com.smart.demo.dto.WordDto;
import com.smart.demo.dto.WordTestDto;
import com.smart.demo.entity.TestResultEntity;
import com.smart.demo.entity.UserEntity;
import com.smart.demo.entity.WordEntity;
import com.smart.demo.entity.WordTestEntity;
import com.smart.demo.repository.TestResultRepository;
import com.smart.demo.repository.UserRepository;
import com.smart.demo.repository.WordRepository;
import com.smart.demo.repository.WordTestRepository;
import com.smart.demo.service.UserService;
import com.smart.demo.service.WordService;
import com.smart.demo.service.WordTestService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class QuizController {
    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

    @Autowired
    private WordService wordService;

    @Autowired
    private UserService userService;
    @Autowired
    private WordTestRepository wordTestRepository;
    @Autowired
    private TestResultRepository testResultRepository;
    @Autowired
    private WordTestService wordTestService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public QuizController(WordService wordService, TestResultRepository testResultRepository, WordTestService wordTestService) {
        this.wordService = wordService;
        this.testResultRepository = testResultRepository;
        this.wordTestService = wordTestService;
    }

    @GetMapping("/App/level_remember")
    public String level_rememberForm() {
        return "App/level_remember";
    }

    @GetMapping("/App/level_test")
    public String level_testForm() {
        return "App/level_test";
    }

    @GetMapping("/App/TestSelectLevel")
    public String TestSelectLevel() {
        return "App/TestSelectLevel";
    }

    @GetMapping("/App/MemorizeSelectMode/{idx}")
    public String MemorizeSelectMode(@PathVariable Integer idx, Model model) {
        UserDto userDto = userService.findById(idx);
        model.addAttribute("user", userDto);
        return "App/MemorizeSelectMode";
    }

    @GetMapping("/App/MemorizeSelectLevel")
    public String MemorizeSelectLevel() {
        return "App/MemorizeSelectLevel";
    }

    @GetMapping("/App/ResultSelectMode")
    public String ResultSelectMode() {
        return "App/ResultSelectMode";
    }

    @GetMapping("/App/ResultSelectLevel")
    public String ResultSelectLevel() {
        return "App/ResultSelectLevel";
    }

    @GetMapping("/App/Quiz/Memorize/{wordLevel}")
    public String MemorizeByLevel(@PathVariable int wordLevel, Model model, HttpSession session) {
        List<WordDto> wordList = wordService.findByWordLevel(wordLevel);

        if (wordList.isEmpty()) {
            model.addAttribute("message", "해당 단계에 단어가 없습니다.");
            return "App/WordQuizResult"; // 빈 화면 또는 적절한 페이지로 리다이렉트
        }

        session.setAttribute("wordList", wordList);
        session.setAttribute("solvedWords", new ArrayList<Integer>());
        session.setAttribute("solvedCount", 0);
        session.setAttribute("wordLevel", wordLevel);

        return "redirect:/App/Quiz/Memorize"; // 랜덤 퀴즈 페이지로 이동
    }

    @GetMapping("/App/Quiz/Memorize")
    public String randomQuizMemorize(Model model, HttpSession session) {
        List<Integer> solvedWords = (List<Integer>) session.getAttribute("solvedWords");
        List<WordDto> wordList = (List<WordDto>) session.getAttribute("wordList");

        // 풀었던 단어 목록을 초기화하거나, 최초 접근인 경우에는 생성
        if (solvedWords == null) {
            solvedWords = new ArrayList<>();
        }

        WordDto randomWord = wordService.findRandomWordFromList(wordList, solvedWords);
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

            return "App/WordQuizMemorize"; // 단어 맞추기 및 결과 페이지로 이동
        } else {
            // 단어가 없을 경우 처리
            return "redirect:/Admin/Word"; // 리스트 페이지로 이동
        }
    }

    @PostMapping("/checkAnswerMemorize")
    public String checkAnswerMemorize(@RequestParam Integer wordIdx, @RequestParam String userAnswer, Model model, HttpSession session) {
        // 단어 ID를 사용하여 정답을 가져옵니다.
        WordDto wordDto = wordService.findById(wordIdx);

        if (wordDto != null) {
            String[] meanings = wordDto.getWordMean().split(", ");
            String[] names = wordDto.getWordName().split(", ");
            boolean isCorrect = Arrays.stream(meanings)
                    .anyMatch(meaning -> meaning.equalsIgnoreCase(userAnswer));
            boolean isCorrect2 = Arrays.stream(names)
                    .anyMatch(name -> name.equalsIgnoreCase(userAnswer));
            if (isCorrect) {
                model.addAttribute("resultMessage", "정답입니다!");
            } else if(isCorrect2){
                model.addAttribute("resultMessage2", "정답입니다!");
            } else {
                model.addAttribute("resultMessage2", "틀렸습니다. 정답은 " + wordDto.getWordMean() + "입니다.");
            }
        } else {
            model.addAttribute("resultMessage", "단어를 찾을 수 없습니다.");
        }

        // 퀴즈 모드를 유지하고 답 검증 결과를 표시합니다.
        model.addAttribute("word", wordDto);
        model.addAttribute("quizMode", true);

        return "App/WordQuizMemorize";
    }

    // 새로운 퀴즈 문제를 불러오는 메서드 추가
    @GetMapping("/App/Quiz/NextMemorize")
    public String nextQuizMemorize(Model model, HttpSession session) {
        return randomQuizMemorize(model, session); // 기존 randomQuiz 메서드를 재사용
    }

    // 랜덤 단어 페이지로 이동
    @GetMapping("/App/Quiz/Random")
    public String randomQuiz(Model model, HttpSession session) {
        List<Integer> solvedWords = (List<Integer>) session.getAttribute("solvedWords");
        List<WordDto> wordList = (List<WordDto>) session.getAttribute("wordList");

        // 풀었던 단어 목록을 초기화하거나, 최초 접근인 경우에는 생성
        if (solvedWords == null) {
            solvedWords = new ArrayList<>();
        }

        // 풀지 않은 단어가 30개 이상인 경우에는 solvedWords를 초기화
        if (solvedWords.size() >= 30) {
            solvedWords.clear();
        }

        WordDto randomWord = wordService.findRandomWordFromList(wordList, solvedWords);
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

            return "App/WordQuizResult"; // 단어 맞추기 및 결과 페이지로 이동
        } else {
            // 단어가 없을 경우 처리
            return "redirect:/Admin/Word"; // 리스트 페이지로 이동
        }
    }

    @Transactional
    @PostMapping("/checkAnswer")
    public String checkAnswer(@RequestParam Integer wordIdx, @RequestParam String userAnswer,
                              Model model, HttpSession session) {
        WordDto wordDto = wordService.findById(wordIdx);
        List<WordDto> wordList = (List<WordDto>) session.getAttribute("wordList");
        Integer solvedCount = (Integer) session.getAttribute("solvedCount");

        if (solvedCount == null) {
            solvedCount = 1;
        } else {
//            solvedCount++;
        }

        String testNum = "Q" + solvedCount;

        WordTestEntity wordTestEntity = (WordTestEntity) session.getAttribute("wordTestEntity");
        if (wordTestEntity == null) {
            return "error"; // 적절한 오류 페이지로 리다이렉트 또는 리턴
        }

        String ox = "X";
        if (wordDto != null) {
            String[] meanings = wordDto.getWordMean().split(", ");
            boolean isCorrect = Arrays.stream(meanings)
                    .anyMatch(meaning -> meaning.equalsIgnoreCase(userAnswer));

            if (isCorrect) {
                ox = "O";
                model.addAttribute("resultMessage", "정답입니다!");
            } else {
                model.addAttribute("resultMessage2", "틀렸습니다. 정답은 " + wordDto.getWordMean() + "입니다.");
            }

            // TestResultEntity 생성 및 저장
            TestResultEntity testResultEntity = saveTestResultAndWord(testNum, userAnswer, ox, wordTestEntity, wordDto);

            List<TestResultEntity> testResults = (List<TestResultEntity>) session.getAttribute("testResults");
            if (testResults == null) {
                testResults = new ArrayList<>();
            }
            testResults.add(testResultEntity);
            session.setAttribute("testResults", testResults);

            // WordTestEntity의 wordInfo 설정
            WordEntity wordEntity = wordService.convertToEntity(wordDto);
            wordTestEntity.setWordInfo(wordEntity);
            wordTestRepository.save(wordTestEntity);
        } else {
            model.addAttribute("resultMessage", "단어를 찾을 수 없습니다.");
        }

        session.setAttribute("solvedCount", solvedCount);
        session.setAttribute("wordList", wordList);

        model.addAttribute("word", wordDto);
        model.addAttribute("quizMode", true);
        model.addAttribute("solvedCount", solvedCount);
        model.addAttribute("lastQuestion", solvedCount.equals(session.getAttribute("questionCount")));

        return "App/WordQuizResult"; // 결과 페이지로 이동
    }


    /**
     * TestResultEntity를 저장하고 WordEntity의 관계를 설정하여 반환하는 메서드
     */
    private TestResultEntity saveTestResultAndWord(String testNum, String userAnswer, String ox,
                                                   WordTestEntity wordTestEntity, WordDto wordDto) {

        TestResultEntity testResultEntity = new TestResultEntity();
        testResultEntity.setTestNum(testNum);
        testResultEntity.setAnswer(userAnswer);
        testResultEntity.setOx(ox);
        testResultEntity.setWordTestEntity(wordTestEntity); // wordTestEntity 설정

        // WordEntity와의 관계 설정
        if (wordDto != null) {
            WordEntity wordEntity = new WordEntity();
            wordEntity.setIdx(wordDto.getIdx()); // 예시에서는 WordDto의 idx를 WordEntity의 idx로 설정한 것으로 가정
            testResultEntity.setWordIdx(wordEntity);
        }

        // TestResultEntity 저장
        return testResultRepository.save(testResultEntity);
    }

    @GetMapping("/startTest")
    public String startTest(
            @RequestParam Integer wordLevel,
            @RequestParam int questionCount,
            Model model,
            HttpSession session) {

        String userUuid = (String) session.getAttribute("userUuid");

        // wordLevel과 questionCount를 기반으로 단어 리스트 가져오기
        List<WordDto> wordList = wordService.findByWordLevel(wordLevel);

        if (wordList.isEmpty()) {
            model.addAttribute("message", "해당 단계에 단어가 없습니다.");
            return "App/WordQuizResult"; // 또는 적절한 페이지로 리다이렉트
        }

        // 문제 수에 맞게 단어 리스트를 잘라내기
        if (wordList.size() > questionCount) {
            Collections.shuffle(wordList); // 단어 리스트를 랜덤하게 섞기
            wordList = wordList.subList(0, questionCount); // 지정한 문제 수 만큼 리스트 자르기
        }

        // userUuid를 사용하여 UserEntity 조회
        UserEntity userEntity = userRepository.findByUserUuid(userUuid);
        if (userEntity == null) {
            model.addAttribute("message", "사용자 정보가 없습니다.");
            return "App/WordQuizResult"; // 또는 적절한 페이지로 리다이렉트
        }

        // WordTestEntity 생성 및 저장
        WordTestEntity wordTestEntity = new WordTestEntity();
        wordTestEntity.setLevel(wordLevel); // 시험의 단계 설정
        wordTestEntity.setTestPoint(0); // 초기 점수 0 설정
        wordTestEntity.setUserInfo(userEntity); // UserEntity 설정
        wordTestEntity = wordTestRepository.save(wordTestEntity); // 저장된 엔티티로 업데이트

        // 세션에 필요한 정보 저장
        session.setAttribute("wordTestEntity", wordTestEntity);
        session.setAttribute("wordList", wordList);
        session.setAttribute("solvedWords", new ArrayList<>());
        session.setAttribute("solvedCount", 0);
        session.setAttribute("questionCount", questionCount);

        // 테스트 시작 페이지로 리다이렉트
        return "redirect:/App/Quiz/Random";
    }

    @PostMapping("/App/Quiz/endTest")
    @Transactional
    public String endTest(HttpSession session) {
        // 세션에서 필요한 데이터 가져오기
        WordTestEntity wordTestEntity = (WordTestEntity) session.getAttribute("wordTestEntity");
        List<TestResultEntity> testResults = (List<TestResultEntity>) session.getAttribute("testResults");

        // 시험 점수 계산 예시 (정답 개수 기준으로 점수 계산)
        long correctCount = testResults.stream().filter(result -> "O".equals(result.getOx())).count();
        int score = (int) ((correctCount / (double) testResults.size()) * 100);
        wordTestEntity.setTestPoint(score);

        // WordTestEntity 업데이트
        wordTestRepository.save(wordTestEntity);

        // TestResultEntity의 wordIdx 설정 유지
        for (TestResultEntity testResult : testResults) {
            // 특별히 변경할 필요 없음
            testResultRepository.save(testResult); // 테스트 결과 저장
        }

        // 세션 초기화
        session.removeAttribute("wordTestEntity");
        session.removeAttribute("testResults");
        session.removeAttribute("solvedWords");
        session.removeAttribute("solvedCount");

        // wordTestEntity의 idx를 저장
        session.setAttribute("wordTestEntityIdForTestRunner", wordTestEntity.getIdx());

        // userUuid를 세션에 저장
        session.setAttribute("userUuid", wordTestEntity.getUserInfo().getUserUuid());

        return "redirect:/App/testList";
    }


    // 새로운 퀴즈 문제를 불러오는 메서드 추가
    @GetMapping("/App/Quiz/Next")
    public String nextQuiz(Model model, HttpSession session) {
        return randomQuiz(model, session); // 기존 randomQuiz 메서드를 재사용
    }


    @GetMapping("/App/testList")
    public String listTests(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WordTestEntity> testPage = wordTestRepository.findAllWithWordInfo(pageable);

        // 각 시험의 wordInfo에서 wordLevel을 가져와서 세팅
        for (WordTestEntity test : testPage) {
            if (test.getWordInfo() != null) {
                test.setLevel(test.getWordInfo().getWordLevel());
            }
        }

        model.addAttribute("testPage", testPage);
        return "/App/testList";
    }


    // QuizController.java
    @GetMapping("/App/testDetail/{id}")
    @Transactional
    public String detailTest(@PathVariable("id") Integer wordTestEntityId, Model model) {
        try {
            List<TestResultEntity> results = wordTestService.getTestResultsByWordTestEntityId(wordTestEntityId);

            for (TestResultEntity result : results) {
                WordEntity wordInfo = result.getWordIdx(); // TestResultEntity의 wordInfo 가져오기
                if (wordInfo != null) {
                    result.getWordTestEntity().setWordInfo(wordInfo); // WordTestEntity에 WordEntity 설정
                }
            }

            model.addAttribute("resultsWithWordInfo", results);
            return "App/testDetail";
        } catch (Exception e) {
            logger.error("Error retrieving test results for wordTestEntityId: {}", wordTestEntityId, e);
            model.addAttribute("errorMessage", "An error occurred while retrieving test results.");
            return "error";
        }
    }





//    @GetMapping("/App/Quiz/Submit")
//    public String submitQuiz(HttpSession session) {
//        session.removeAttribute("solvedCount"); // 풀이한 문제 수 초기화
//        session.removeAttribute("currentQuestionNumber"); // 현재 문제 번호 초기화
//        session.removeAttribute("testOxEntity"); // 시험 결과 초기화
//        return "redirect:/main2"; // 메인 화면으로 이동
//    }
}
