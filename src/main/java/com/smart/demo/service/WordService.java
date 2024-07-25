package com.smart.demo.service;

import com.smart.demo.dto.WordDto;
import com.smart.demo.entity.WordEntity;
import com.smart.demo.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WordService {
    private final WordRepository wordRepository;

    public void save(WordDto wordDto) {
        WordEntity wordEntity = WordEntity.toSaveEntity(wordDto);
        wordRepository.save(wordEntity);
    }

    public List<WordDto> findAll() {
        List<WordEntity> wordEntityList = wordRepository.findAll();
        List<WordDto> wordDtoList = new ArrayList<>();
        for(WordEntity wordEntity: wordEntityList) {
            wordDtoList.add(WordDto.toWordDto(wordEntity));
        }
        return wordDtoList;
    }

//    public WordDto findById(Long words_idx) {
//        Optional<WordEntity> optionalWordEntity = wordRepository.findById(words_idx);
//        if(optionalWordEntity.isEmpty()) {
//            WordEntity wordEntity = optionalWordEntity.get();
//            WordDto wordDto = WordDto.toWordDto(wordEntity);
//            return wordDto;
//        } else {
//            return null;
//        }
//    }
    public WordDto findById(Integer idx) {
        Optional<WordEntity> optionalWordEntity = wordRepository.findById(idx);
        if(optionalWordEntity.isPresent()) {
            WordDto wordDto = new WordDto();
            wordDto.setIdx(optionalWordEntity.get().getIdx());
            wordDto.setWordName(optionalWordEntity.get().getWordName());
            wordDto.setWordMean(optionalWordEntity.get().getWordMean());
            wordDto.setWordLevel(optionalWordEntity.get().getWordLevel());
            wordDto.setWordDetail(optionalWordEntity.get().getWordDetail());
            return wordDto;
        } else {
            return null;
        }
    }

    public WordDto update(WordDto wordDto) {
        WordDto updateDto = null;
        Optional<WordEntity> wordEntityOptional = wordRepository.findById(wordDto.getIdx());
        if(wordEntityOptional.isPresent()) {
            WordEntity wordEntity = null;
            wordEntity = wordEntityOptional.get();
            wordEntity.setWordName(wordDto.getWordName());
            wordEntity.setWordLevel(wordDto.getWordLevel());
            wordEntity.setWordMean(wordDto.getWordMean());
            wordEntity.setWordDetail(wordDto.getWordDetail());

            wordEntity = wordRepository.save(wordEntity);
            updateDto = WordDto.toWordDto(wordEntity);
        }
        return updateDto;
    }

    public void delete(Integer idx) {
        wordRepository.deleteById(idx);
    }

    public WordDto findRandomWord(List<Integer> solvedWords) {
        List<WordEntity> wordEntityList = wordRepository.findAll();

        if (wordEntityList.isEmpty()) {
            return null; // 단어가 없을 경우 처리
        }

        // 제외할 단어를 필터링
        List<WordEntity> filteredList = wordEntityList.stream()
                .filter(word -> solvedWords == null || !solvedWords.contains(word.getIdx()))
                .collect(Collectors.toList());

        if (filteredList.isEmpty()) {
            return null; // 모든 단어가 제외된 경우 처리
        }

        // 랜덤 인덱스 생성
        Random random = new Random();
        int randomIndex = random.nextInt(filteredList.size());

        // 랜덤으로 선택된 단어 반환
        WordEntity randomWordEntity = filteredList.get(randomIndex);
        return WordDto.toWordDto(randomWordEntity);
    }

    public List<WordDto> findByWordLevel(int wordLevel) {
        return wordRepository.findByWordLevel(wordLevel)
                .stream()
                .map(wordEntity -> new WordDto(wordEntity.getIdx(), wordEntity.getWordName(), wordEntity.getWordMean()))
                .collect(Collectors.toList());
    }

    public WordDto findRandomWordFromList(List<WordDto> wordList, List<Integer> solvedWords) {
        List<WordDto> unsolvedWords = wordList.stream()
                .filter(word -> !solvedWords.contains(word.getIdx()))
                .collect(Collectors.toList());

        if (unsolvedWords.isEmpty()) {
            return null;
        }

        Random random = new Random();
        return unsolvedWords.get(random.nextInt(unsolvedWords.size()));
    }

    public WordEntity convertToEntity(WordDto wordDto) {
        WordEntity wordEntity = new WordEntity();
        wordEntity.setWordName(wordDto.getWordName());
        wordEntity.setWordMean(wordDto.getWordMean());
        wordEntity.setWordLevel(wordDto.getWordLevel());
        wordEntity.setWordDetail(wordEntity.getWordDetail());
        wordEntity.setWordAble(wordDto.getWordAble());
        return wordEntity;
    }


}
