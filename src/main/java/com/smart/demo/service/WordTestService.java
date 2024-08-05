    package com.smart.demo.service;

    import com.smart.demo.dto.WordDto;
    import com.smart.demo.entity.TestResultEntity;
    import com.smart.demo.entity.WordEntity;
    import com.smart.demo.entity.WordTestEntity;
    import com.smart.demo.repository.TestResultRepository;
    import com.smart.demo.repository.WordRepository;
    import com.smart.demo.repository.WordTestRepository;
    import jakarta.persistence.EntityManager;
    import jakarta.persistence.EntityNotFoundException;
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
        public TestResultEntity saveTestResultAndWord(WordDto wordDto, WordTestEntity wordTestEntity, String answer, String ox) {
            // WordDto를 기반으로 WordEntity를 가져옵니다.
            WordEntity wordEntity = wordService.findWordEntityById(wordDto.getIdx());

            // WordTestEntity가 이미 존재하는지 확인합니다.
            if (wordTestEntity == null || wordTestEntity.getIdx() == null) {
                throw new IllegalArgumentException("Invalid WordTestEntity provided");
            }

            // TestResultEntity를 데이터베이스에서 가져오기
            TestResultEntity testResultEntity = testResultRepository.findByTestNumAndWordTestEntity(wordDto.getIdx(), wordTestEntity);

            // TestResultEntity가 존재하지 않으면 새로 생성
            if (testResultEntity == null) {
                testResultEntity = new TestResultEntity();
                testResultEntity.setTestNum(wordDto.getIdx());  // 문제의 고유 번호를 설정
                testResultEntity.setWordIdx(wordEntity);  // WordEntity를 설정
                testResultEntity.setWordTestEntity(wordTestEntity);  // WordTestEntity 설정
                testResultEntity.setTestNum(testResultEntity.getTestNum());
            } else {
                // TestResultEntity가 존재하는 경우, detached 상태의 WordTestEntity를 처리하기 위해 merge를 사용
                testResultEntity = entityManager.merge(testResultEntity);
            }

            // 답변이 공백이 아니면 값을 설정, 공백일 경우 기존 값을 유지
            if (answer != null && !answer.trim().isEmpty()) {
                testResultEntity.setAnswer(answer);
                testResultEntity.setOx(ox);
            } else {
                // answer가 공백인 경우, 기존 값 유지
                // ox는 빈 문자열로 설정 (필요 시)
                testResultEntity.setOx(ox);
            }

            // TestResultEntity를 데이터베이스에 저장합니다.
            return testResultRepository.save(testResultEntity);
        }

    }
