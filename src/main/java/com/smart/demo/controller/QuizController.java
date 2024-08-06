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
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class QuizController {
    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

    @Autowired
    private WordService wordService;
    @Autowired
    private WordRepository wordRepository;
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

    @PersistenceContext
    private EntityManager entityManager;

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
        model.addAttribute("user", session);

        return "redirect:/App/Quiz/Memorize"; // 랜덤 퀴즈 페이지로 이동
    }

    @GetMapping("/App/Quiz/Memorize")
    public String randomQuizMemorize(Model model, HttpSession session) {
        List<Integer> solvedWords = (List<Integer>) session.getAttribute("solvedWords");
        List<WordDto> wordList = (List<WordDto>) session.getAttribute("wordList");
        String examMode = (String) session.getAttribute("examMode");

        model.addAttribute("user", session);
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
            String cleanedUserAnswer = userAnswer.replaceAll("[^a-zA-Z0-9가-힣]", ""); // 사용자 입력 정답 정리
            boolean isCorrect = Arrays.stream(meanings)
                    .map(meaning -> meaning.replaceAll("[^a-zA-Z0-9가-힣]", "")) // 데이터베이스 정답 정리
                    .anyMatch(meaning -> meaning.equalsIgnoreCase(cleanedUserAnswer));
            boolean isCorrect2 = Arrays.stream(names)
                    .map(meaning -> meaning.replaceAll("[^a-zA-Z0-9가-힣]", "")) // 데이터베이스 정답 정리
                    .anyMatch(naming -> naming.equalsIgnoreCase(cleanedUserAnswer));
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

    @GetMapping("/App/Quiz/Random")
    public String randomQuiz(Model model, HttpSession session) {
        List<Integer> solvedWords = (List<Integer>) session.getAttribute("solvedWords");
        List<WordDto> wordList = (List<WordDto>) session.getAttribute("wordList");
        String examMode = (String) session.getAttribute("examMode");

        if (solvedWords == null) {
            solvedWords = new ArrayList<>();
        }

        WordDto randomWord = wordService.findRandomWordFromList(wordList, solvedWords, examMode);
        if (randomWord != null) {
            // 단어의 의미를 랜덤으로 선택
            String[] meanings = randomWord.getWordMean().split(", ");
            String selectedMeaning = meanings[new Random().nextInt(meanings.length)];

            model.addAttribute("word", randomWord);
            model.addAttribute("randomMeaning", selectedMeaning); // 선택된 의미 추가
            model.addAttribute("quizMode", true);

            if (!solvedWords.contains(randomWord.getIdx())) {
                solvedWords.add(randomWord.getIdx());
                session.setAttribute("solvedWords", solvedWords);
            }
            int solvedCount = (int) session.getAttribute("solvedCount");
            if(solvedCount == 0) {
                solvedCount = 1;
            }
            session.setAttribute("solvedCount", solvedCount);

            model.addAttribute("solvedCount", solvedCount);
            model.addAttribute("lastQuestion", solvedCount == 30);

            // examMode에 따라 다른 결과 페이지로 이동
            if ("korEng".equals(examMode)) {
                return "App/WordQuizResultKorea";
            } else {
                return "App/WordQuizResult";
            }
        } else {
            return "redirect:/App/testList"; // 리스트 페이지로 이동
        }
    }


    @Transactional
    @PostMapping("/checkAnswer")
    public String checkAnswer(@RequestParam Integer wordIdx, @RequestParam(required = false) String userAnswer,
                              Model model, HttpSession session) {
        // 로그 출력
        System.out.println("Received request for wordIdx: " + wordIdx);

        // 단어 정보를 가져옵니다
        WordDto wordDto = wordService.findById(wordIdx);
        System.out.println("WordDto: " + wordDto);

        // 세션에서 wordList와 solvedCount를 가져옵니다
        List<WordDto> wordList = (List<WordDto>) session.getAttribute("wordList");
        Integer solvedCount = (Integer) session.getAttribute("solvedCount");
        List<Integer> solvedWords = (List<Integer>) session.getAttribute("solvedWords");
        String examMode = (String) session.getAttribute("examMode");

        if (solvedCount == null) {
            solvedCount = 1;
        }

        Integer testNum = solvedCount;

        // 세션에서 WordTestEntity를 가져옵니다
        WordTestEntity wordTestEntity = (WordTestEntity) session.getAttribute("wordTestEntity");
        if (wordTestEntity == null) {
            // WordTestEntity가 세션에 없으면 새로 생성
            wordTestEntity = new WordTestEntity();
            wordTestEntity = wordTestRepository.save(wordTestEntity); // 새 엔티티 저장
            session.setAttribute("wordTestEntity", wordTestEntity); // 세션에 저장
        } else {
            // EntityManager를 사용하여 merge를 수행합니다
            wordTestEntity = wordTestRepository.findById(wordTestEntity.getIdx()).orElse(wordTestEntity);
        }

        // 사용자 답변 정리
        String cleanedUserAnswer = (userAnswer != null && !userAnswer.trim().isEmpty())
                ? userAnswer.replaceAll("[^a-zA-Z0-9가-힣]", "")
                : "X"; // 답변이 없을 경우 공백 설정

        String ox = "X";

        if (wordDto != null) {
            String[] meanings = wordDto.getWordMean().split(", ");
            boolean isCorrect = Arrays.stream(meanings)
                    .map(meaning -> meaning.replaceAll("[^a-zA-Z0-9가-힣]", "")) // 데이터베이스 정답 정리
                    .anyMatch(meaning -> meaning.equalsIgnoreCase(cleanedUserAnswer));

            if (isCorrect) {
                ox = "O";
                model.addAttribute("resultMessage", "정답입니다!");

                // 정답인 경우 점수를 증가시킵니다
                Integer testPoint = wordTestEntity.getTestPoint();
                if (testPoint == null) {
                    testPoint = 0;
                }

                // 문제당 점수 계산 (예: 100점 만점)
                Integer totalQuestions = wordTestEntity.getQuestionCount();
                int pointsPerQuestion = 100 / totalQuestions; // 각 문제당 점수
                testPoint += pointsPerQuestion; // 정답 점수 증가

                wordTestEntity.setTestPoint(testPoint);
                wordTestRepository.save(wordTestEntity);
            } else if (cleanedUserAnswer.trim().isEmpty()) {
                ox = "X"; // 답변이 비어 있는 경우 "X"로 처리
                model.addAttribute("resultMessage2", "정답을 입력하지 않았습니다.");
            } else {
                model.addAttribute("resultMessage2", "틀렸습니다. 정답은 " + wordDto.getWordMean() + "입니다.");
            }

            // TestResultEntity 생성 및 저장
            // solvedCount를 testNum으로 사용합니다.
            TestResultEntity testResultEntity = saveTestResultAndWord(solvedCount, cleanedUserAnswer, ox, wordTestEntity, wordDto);
            System.out.println("TestResultEntity created: " + testResultEntity);

            // 세션에서 TestResultEntity 리스트를 가져오고 업데이트합니다
            List<TestResultEntity> testResults = (List<TestResultEntity>) session.getAttribute("testResults");
            if (testResults == null) {
                testResults = new ArrayList<>();
            }
            testResults.add(testResultEntity);
            session.setAttribute("testResults", testResults);

            // 푼 문제 목록에 현재 문제를 추가합니다.
            if (solvedWords == null) {
                solvedWords = new ArrayList<>();
            }
            solvedWords.add(wordDto.getIdx());
            session.setAttribute("solvedWords", solvedWords);

            // solvedCount를 업데이트합니다.
            Integer totalQuestions = wordTestEntity.getQuestionCount(); // 전체 문제 개수 가져오기
            if (totalQuestions == null) {
                totalQuestions = wordList.size();
                wordTestEntity.setQuestionCount(totalQuestions);
                wordTestRepository.save(wordTestEntity);
            }

            System.out.println("Solved Count: " + solvedCount);
            System.out.println("Total Questions: " + totalQuestions);

            // 마지막 문제 여부 확인
            boolean isLastQuestion = (solvedCount.equals(totalQuestions));

            if (solvedCount < totalQuestions) {
                solvedCount++;
                session.setAttribute("solvedCount", solvedCount);
            }

            // 마지막 문제 여부를 모델에 추가
            model.addAttribute("lastQuestion", isLastQuestion);

            if (solvedCount == totalQuestions + 1) {
                return "redirect:/App/testList";
            }

            if (isLastQuestion) {
                // 마지막 문제를 푼 경우
                model.addAttribute("resultMessage", "시험이 종료되었습니다.");
                session.setAttribute("finalScore", wordTestEntity.getTestPoint()); // 최종 점수를 세션에 저장
                return "redirect:/App/testList"; // 결과 페이지로 이동
            } else {
                // 다음 문제를 찾습니다.
                WordDto randomWordDto = wordService.findRandomWordFromList(wordList, solvedWords, (String) session.getAttribute("examMode"));

                // 문제가 없을 경우, 리스트 페이지로 리다이렉트합니다.
                if (randomWordDto != null) {
                    // 단어의 의미를 랜덤으로 선택

                    // 모델에 단어와 선택된 의미를 추가합니다.
                    model.addAttribute("word", randomWordDto);
                    model.addAttribute("quizMode", true);

                    // 마지막 문제 여부를 모델에 추가
                    model.addAttribute("solvedCount", solvedCount);
                    model.addAttribute("totalQuestion", totalQuestions);
                    return "App/WordQuizResult"; // 다음 문제 페이지로 이동
                } else {
                    // 문제가 없을 경우, 리스트 페이지로 리다이렉트합니다.
                    return "redirect:/App/testList";
                }
            }
        } else {
            model.addAttribute("resultMessage", "단어를 찾을 수 없습니다.");
            return "App/testList"; // 오류 페이지로 이동
        }
    }


    @Transactional
    @PostMapping("/checkAnswerKorea")
    public String checkAnswerKorea(@RequestParam Integer wordIdx, @RequestParam(required = false) String userAnswer,
                                   Model model, HttpSession session) {
        // 로그 출력
        System.out.println("Received request for wordIdx: " + wordIdx);

        // 단어 정보를 가져옵니다
        WordDto wordDto = wordService.findById(wordIdx);
        System.out.println("WordDto: " + wordDto);

        if (wordDto == null) {
            model.addAttribute("resultMessage", "단어를 찾을 수 없습니다.");
            return "App/testList"; // 오류 페이지로 이동
        }

        // 세션에서 필요한 데이터 가져오기
        List<WordDto> wordList = (List<WordDto>) session.getAttribute("wordList");
        Integer solvedCount = (Integer) session.getAttribute("solvedCount");
        List<Integer> solvedWords = (List<Integer>) session.getAttribute("solvedWords");
        String examMode = (String) session.getAttribute("examMode");

        if (solvedCount == null) {
            solvedCount = 1;
        }

        // 세션에서 WordTestEntity 가져오기
        WordTestEntity wordTestEntity = (WordTestEntity) session.getAttribute("wordTestEntity");
        if (wordTestEntity == null) {
            wordTestEntity = new WordTestEntity();
            wordTestEntity = wordTestRepository.save(wordTestEntity);
            session.setAttribute("wordTestEntity", wordTestEntity);
        } else {
            wordTestEntity = wordTestRepository.findById(wordTestEntity.getIdx()).orElse(wordTestEntity);
        }

        // 사용자 답변 정리
        String cleanedUserAnswer = (userAnswer != null && !userAnswer.trim().isEmpty())
                ? userAnswer.replaceAll("[^a-zA-Z]", "") // 영어 문자만 허용
                : "X";

        String ox = "X";

        // 정답 검증
        String[] names = wordDto.getWordName().split(", ");
        boolean isCorrect = Arrays.stream(names)
                .map(name -> name.replaceAll("[^a-zA-Z]", "")) // 데이터베이스 정답 정리
                .anyMatch(name -> name.equalsIgnoreCase(cleanedUserAnswer));

        if (isCorrect) {
            ox = "O";
            model.addAttribute("resultMessage", "정답입니다!");

            // 정답인 경우 점수 증가
            Integer testPoint = wordTestEntity.getTestPoint();
            if (testPoint == null) {
                testPoint = 0;
            }

            Integer totalQuestions = wordTestEntity.getQuestionCount();
            int pointsPerQuestion = 100 / totalQuestions; // 각 문제당 점수
            testPoint += pointsPerQuestion; // 정답 점수 증가

            wordTestEntity.setTestPoint(testPoint);
            wordTestRepository.save(wordTestEntity);
        } else if (cleanedUserAnswer.trim().isEmpty()) {
            ox = "X";
            model.addAttribute("resultMessage2", "정답을 입력하지 않았습니다.");
        } else {
            model.addAttribute("resultMessage2", "틀렸습니다. 정답은 " + wordDto.getWordName() + "입니다.");
        }

        // TestResultEntity 생성 및 저장
        TestResultEntity testResultEntity = saveTestResultAndWord(solvedCount, cleanedUserAnswer, ox, wordTestEntity, wordDto);
        System.out.println("TestResultEntity created: " + testResultEntity);

        // 세션에서 TestResultEntity 리스트를 가져오고 업데이트
        List<TestResultEntity> testResults = (List<TestResultEntity>) session.getAttribute("testResults");
        if (testResults == null) {
            testResults = new ArrayList<>();
        }
        testResults.add(testResultEntity);
        session.setAttribute("testResults", testResults);

        // 푼 문제 목록에 현재 문제를 추가
        if (solvedWords == null) {
            solvedWords = new ArrayList<>();
        }
        solvedWords.add(wordDto.getIdx());
        session.setAttribute("solvedWords", solvedWords);

        // solvedCount 업데이트
        Integer totalQuestions = wordTestEntity.getQuestionCount();
        if (totalQuestions == null) {
            totalQuestions = wordList.size();
            wordTestEntity.setQuestionCount(totalQuestions);
            wordTestRepository.save(wordTestEntity);
        }

        boolean isLastQuestion = (solvedCount.equals(totalQuestions));

        if (solvedCount < totalQuestions) {
            solvedCount++;
            session.setAttribute("solvedCount", solvedCount);
        }

        model.addAttribute("lastQuestion", isLastQuestion);

        if (solvedCount == totalQuestions + 1) {
            return "redirect:/App/testList";
        }

        if (isLastQuestion) {
            model.addAttribute("resultMessage", "시험이 종료되었습니다.");
            session.setAttribute("finalScore", wordTestEntity.getTestPoint()); // 최종 점수를 세션에 저장
            return "redirect:/App/testList"; // 결과 페이지로 이동
        } else {
            // Redirect to randomQuiz endpoint with examMode parameter
            return "redirect:/App/Quiz/Random?examMode=" + examMode;
        }
    }




    /**
     * TestResultEntity를 저장하고 WordEntity의 관계를 설정하여 반환하는 메서드
     */
    @Transactional
    public TestResultEntity saveTestResultAndWord(Integer testNum, String userAnswer, String ox,
                                                  WordTestEntity wordTestEntity, WordDto wordDto) {
        System.out.println("Saving TestResultEntity with testNum: " + testNum + ", userAnswer: " + userAnswer + ", ox: " + ox);

        // WordTestEntity를 병합하여 영속성 컨텍스트에 다시 연결
        WordTestEntity managedWordTestEntity = wordTestRepository.save(wordTestEntity);
        managedWordTestEntity.setTestPoint(wordTestEntity.getTestPoint());

        TestResultEntity testResultEntity = new TestResultEntity();
        testResultEntity.setTestNum(testNum);
        testResultEntity.setAnswer(userAnswer != null ? userAnswer : " ");
        testResultEntity.setOx(ox);
        testResultEntity.setWordTestEntity(managedWordTestEntity); // 병합된 엔티티 사용

        // WordEntity와의 관계 설정
        if (wordDto != null) {
            WordEntity wordEntity = new WordEntity();
            wordEntity.setIdx(wordDto.getIdx());
            testResultEntity.setWordIdx(wordEntity);
        }

        // TestResultEntity 저장
        TestResultEntity savedEntity = testResultRepository.save(testResultEntity);
        System.out.println("Saved TestResultEntity: " + savedEntity);
        return savedEntity;
    }

    @GetMapping("/startTest")
    public String startTest(
            @RequestParam("examMode") String examMode,
            @RequestParam Integer wordLevel,
            @RequestParam int questionCount,
            Model model,
            HttpSession session) {

        String userUuid = (String) session.getAttribute("userUuid");
        session.setAttribute("examMode", examMode);

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
        wordTestEntity.setQuestionCount(questionCount); // 전체 문제 수 설정

        // examMode에 따라 language 설정
        if ("korEng".equals(examMode)) {
            wordTestEntity.setLanguage(0);
        } else if ("engKor".equals(examMode)) {
            wordTestEntity.setLanguage(1);
        }

        wordTestEntity = wordTestRepository.save(wordTestEntity); // 저장된 엔티티로 업데이트

        // 첫 번째 문제를 설정 (이후 문제를 순차적으로 보여줄 수 있도록 설정)
        WordDto firstWordDto = wordList.get(0);

        // WordEntity를 가져와서 WordTestEntity에 설정
        for (WordDto wordDto : wordList) {
            WordEntity wordEntity = wordRepository.findById(wordDto.getIdx()).orElse(null);
            if (wordEntity != null) {
                wordTestEntity.setWordInfo(wordEntity); // word_info_idx 설정
                wordTestRepository.save(wordTestEntity); // 변경사항 저장
            }
        }

        // 세션에 필요한 정보 저장
        session.setAttribute("wordTestEntity", wordTestEntity);
        session.setAttribute("wordList", wordList);
        session.setAttribute("currentWordDto", firstWordDto); // 현재 문제 설정
        session.setAttribute("solvedWords", new ArrayList<>());
        session.setAttribute("solvedCount", 0);
        session.setAttribute("questionCount", questionCount);

        // 테스트 시작 페이지로 리다이렉트
        return "redirect:/App/Quiz/Random";
    }


    @GetMapping("/App/testList")
    public String testList(Model model,
                           HttpSession session,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        // 세션에서 사용자 UUID를 가져옵니다.
        String userUuid = (String) session.getAttribute("userUuid");
        String examMode = (String) session.getAttribute("examMode");
        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);

        // 사용자의 테스트 목록을 페이징 처리하여 가져옵니다.
        Page<WordTestEntity> testPage = wordTestRepository.findByUserInfo_UserUuid(userUuid, pageable);

        // testPage가 null인지 체크
        if (testPage == null) {
            model.addAttribute("errorMessage", "테스트 목록을 불러오는 데 실패했습니다.");
            return "error"; // 에러 페이지로 이동
        }

        // 시험 목록과 각 시험에 대한 맞춘 문제와 전체 문제 개수를 계산
        for (WordTestEntity test : testPage) {
            List<TestResultEntity> testResults = testResultRepository.findByWordTestEntityIdx(test.getIdx());

            long solvedCount = testResults.stream()
                    .filter(result -> "O".equals(result.getOx()))
                    .count();

            long totalCount = testResults.size();

            test.setSolvedCount((int) solvedCount);
            test.setQuestionCount((int) totalCount);
        }

        model.addAttribute("testPage", testPage);
        model.addAttribute("examMode", examMode);

        return "App/testList";
    }


    // QuizController.java
    @GetMapping("/App/testDetail/{id}")
    @Transactional
    public String detailTest(@PathVariable("id") Integer wordTestEntityId, Model model, HttpSession session) {
        try {
            List<TestResultEntity> results = wordTestService.getTestResultsByWordTestEntityId(wordTestEntityId);
            String examMode = (String) session.getAttribute("examMode");

            WordTestEntity wordTestEntity = wordTestRepository.findById(wordTestEntityId).orElse(null);
            if (wordTestEntity == null) {
                model.addAttribute("errorMessage", "테스트를 찾을 수 없습니다.");
                return "error";
            }

            for (TestResultEntity result : results) {
                WordEntity wordInfo = result.getWordIdx(); // TestResultEntity의 wordInfo 가져오기
                if (wordInfo != null) {
                    result.getWordTestEntity().setWordInfo(wordInfo); // WordTestEntity에 WordEntity 설정
                }
            }
            // 단일 createdTime을 설정, 예를 들어 첫 번째 결과의 createdTime을 사용
            if (!results.isEmpty()) {
                LocalDateTime createdTime = results.get(0).getCreatedTime();
                model.addAttribute("createdTime", createdTime);
            }

            // 언어 정보 모델에 추가
            int language = wordTestEntity.getLanguage();
            model.addAttribute("language", language);

            model.addAttribute("examMode", examMode);
            model.addAttribute("resultsWithWordInfo", results);
            return "App/testDetail";
        } catch (Exception e) {
            logger.error("Error retrieving test results for wordTestEntityId: {}", wordTestEntityId, e);
            model.addAttribute("errorMessage", "An error occurred while retrieving test results.");
            return "error";
        }
    }

    @PostMapping("/App/goBack")
    public String goBack(HttpSession session, Model model) {
        List<Integer> solvedWords = (List<Integer>) session.getAttribute("solvedWords");
        if (solvedWords == null || solvedWords.isEmpty()) {
            model.addAttribute("resultMessage", "이전 문제를 찾을 수 없습니다.");
            return "App/level_test";
        }

        Integer currentWordIdx = (Integer) session.getAttribute("currentWordIdx");
        if (currentWordIdx == null) {
            model.addAttribute("resultMessage", "이전 문제를 찾을 수 없습니다.");
            return "App/level_test";
        }

        // 현재 문제 인덱스 찾기
        int currentIndex = solvedWords.indexOf(currentWordIdx);

        if (currentIndex > 0) {
            Integer previousWordIdx = solvedWords.get(currentIndex - 1);
            WordDto previousWordDto = wordService.findById(previousWordIdx);

            model.addAttribute("word", previousWordDto);
            model.addAttribute("quizMode", true);

            // 이전 문제 인덱스를 세션에 저장
            session.setAttribute("currentWordIdx", previousWordIdx);

            return "App/WordQuizResult";
        } else {
            model.addAttribute("resultMessage", "이전 문제를 찾을 수 없습니다.");
            return "App/level_test";
        }
    }

    @GetMapping("/App/getCurrentWordIdx")
    @ResponseBody
    public Map<String, Integer> getCurrentWordIdx(HttpSession session) {
        Integer currentWordIdx = (Integer) session.getAttribute("currentWordIdx");
        if (currentWordIdx == null) {
            currentWordIdx = -1; // 기본값 설정 (문제 없음)
        }
        Map<String, Integer> response = new HashMap<>();
        response.put("currentWordIdx", currentWordIdx);
        return response;
    }



}
