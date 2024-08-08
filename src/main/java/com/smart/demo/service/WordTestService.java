    package com.smart.demo.service;

    import com.smart.demo.entity.TestResultEntity;;
    import com.smart.demo.entity.WordTestEntity;
    import com.smart.demo.repository.TestResultRepository;
    import com.smart.demo.repository.WordRepository;
    import com.smart.demo.repository.WordTestRepository;
    import jakarta.persistence.EntityManager;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.List;

    @Service
    @Transactional
    public class WordTestService {

        private final WordTestRepository wordTestRepository;
        private final TestResultRepository testResultRepository;
        private final WordRepository wordRepository;
        private final WordService wordService;

        @Autowired
        private EntityManager entityManager;

        @Autowired
        public WordTestService(WordTestRepository wordTestRepository, TestResultRepository testResultRepository, WordRepository wordRepository, WordService wordService) {
            this.wordTestRepository = wordTestRepository;
            this.testResultRepository = testResultRepository;
            this.wordRepository = wordRepository;
            this.wordService = wordService;
        }

        public List<TestResultEntity> getTestResultsByWordTestEntityId(Integer wordTestEntityId) {
            return testResultRepository.findByWordTestEntityIdWithWordInfo(wordTestEntityId);
        }

        @Transactional(readOnly = true)
        public WordTestEntity findWordTestEntityById(Integer id) {
            return wordTestRepository.findById(id).orElse(null);
        }

        @Transactional
        public void disableTest(Integer idx) {
            WordTestEntity wordTest = wordTestRepository.findById(idx)
                    .orElseThrow(() -> new RuntimeException("Test not found"));

            wordTest.setTestAble(0);
            wordTestRepository.save(wordTest);
        }
    }
