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

    public List<WordDto> findByWordLevel(int wordLevel) {
        return wordRepository.findByWordLevel(wordLevel)
                .stream()
                .map(wordEntity -> new WordDto(wordEntity.getIdx(), wordEntity.getWordName(), wordEntity.getWordMean()))
                .collect(Collectors.toList());
    }

    public WordDto findRandomWordFromList(List<WordDto> wordList, List<Integer> solvedWords, String examMode) {
        List<WordDto> unsolvedWords = wordList.stream()
                .filter(word -> !solvedWords.contains(word.getIdx()))
                .collect(Collectors.toList());

        if (unsolvedWords.isEmpty()) {
            return null;
        }

        WordDto randomWord = unsolvedWords.get(new Random().nextInt(unsolvedWords.size()));

        if ("korEng".equals(examMode)) {
            // 무작위로 의미 선택 (한글 의미를 선택)
            String[] namings = randomWord.getWordName().split(", ");
            String selectedNamings = namings[new Random().nextInt(namings.length)];
            randomWord.setSelectedWordName(selectedNamings); // 한글 의미를 저장
        }

        return randomWord;
    }


    public WordEntity findWordEntityById(Integer idx) {
        WordEntity wordEntity = wordRepository.findById(idx)
                .orElseThrow(() -> new RuntimeException("WordEntity not found with id " + idx));
        System.out.println("Debug: Found WordEntity: " + wordEntity); // Debug statement
        return wordEntity;
    }

}
