package com.smart.demo;

import com.smart.demo.entity.TestResultEntity;
import com.smart.demo.entity.WordEntity;
import com.smart.demo.entity.WordTestEntity;
import com.smart.demo.repository.TestResultRepository;
import com.smart.demo.repository.WordTestRepository;
import com.smart.demo.service.WordTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class TestRunner implements CommandLineRunner {
    @Autowired
    private WordTestService wordTestService;
    @Autowired
    private TestResultRepository testResultRepository;
    @Autowired
    private WordTestRepository wordTestRepository;

    @Override
    public void run(String... args) throws Exception {
        Integer wordTestEntityId = 1;

        WordTestEntity wordTestEntity = wordTestService.findWordTestEntityById(wordTestEntityId);

        if (wordTestEntity == null) {
            System.out.println("WordTestEntity not found for ID: " + wordTestEntityId);
            return;
        }

        List<TestResultEntity> testResults = wordTestEntity.getTestResults();

        if (testResults.isEmpty()) {
            System.out.println("No test results found for WordTestEntity ID: " + wordTestEntityId);
        } else {
            testResults.forEach(result -> {
                Integer testResultIdx = result.getIdx();
                String answer = result.getAnswer();
                LocalDateTime createdTime = result.getCreatedTime();
                String ox = result.getOx();
                WordEntity wordIdx = result.getWordIdx();

                if (wordIdx == null) {
                    System.out.println("WordIdx is null for Test Result ID: " + testResultIdx);
                    return;
                }

                String wordName = wordIdx.getWordName();
                String wordMean = wordIdx.getWordMean();

                System.out.println("Test Result ID: " + testResultIdx);
                System.out.println("Word Name: " + wordName);
                System.out.println("Word Mean: " + wordMean);
                System.out.println("Answer: " + answer);
                System.out.println("OX: " + ox);
                System.out.println("Created Time: " + createdTime);
                System.out.println("------------------------------");
            });
        }
    }

}
