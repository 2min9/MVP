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
            wordDto.setWords_name(optionalWordEntity.get().getWords_name());
            wordDto.setWords_mean(optionalWordEntity.get().getWords_mean());
            wordDto.setWords_difficulty(optionalWordEntity.get().getWords_difficulty());
            wordDto.setWords_similar(optionalWordEntity.get().getWords_similar());
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
            wordEntity.setWords_name(wordDto.getWords_name());
            wordEntity.setWords_difficulty(wordDto.getWords_difficulty());
            wordEntity.setWords_mean(wordDto.getWords_mean());
            wordEntity.setWords_similar(wordDto.getWords_similar());
            wordEntity.setWords_pronunciation(wordDto.getWords_pronunciation());

            wordEntity = wordRepository.save(wordEntity);
            updateDto = WordDto.toWordDto(wordEntity);
        }
        return updateDto;
    }

    public void delete(Integer idx) {
        wordRepository.deleteById(idx);
    }
}
