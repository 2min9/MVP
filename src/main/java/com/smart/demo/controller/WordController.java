package com.smart.demo.controller;

import com.smart.demo.dto.WordDto;
import com.smart.demo.repository.WordRepository;
import com.smart.demo.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Transactional
public class WordController {
    private final WordService wordService;
    private final WordRepository wordRepository;

    @GetMapping("/Admin/Word")
    public String Word(Model model) {
        List<WordDto> wordDtoList = wordService.findAll();
        model.addAttribute("wordList", wordDtoList);
        return "Admin/Word";
    }

    @GetMapping("/Admin/WordSave")
    public String savepage() {
        return "Admin/WordSave";
    }

    @PostMapping("/Admin/Save")
    public String save(@ModelAttribute WordDto wordDto, Model model) {
        List<WordDto> wordDtoList = wordService.findAll();
        model.addAttribute("wordList", wordDtoList);
        System.out.println("wordDto = " + wordDto);
        wordService.save(wordDto);
        return "redirect:/Admin/Word";
    }

//    @GetMapping("/Admin/Word/{id}")
//    public String findById(@PathVariable Long words_idx, Model model) {
//        WordDto wordDto = wordService.findById(words_idx);
//        model.addAttribute("word", wordDto);
//
//        return "/Admin/Word_detail";
//    } // 단어 상세보기

    @GetMapping("/Admin/update/{idx}")
    public String updateForm(@PathVariable Integer idx, Model model) {
        WordDto wordDto = wordService.findById(idx);
        model.addAttribute("word", wordDto);
        return "Admin/WordUpdate";
    }

    @PostMapping("/Admin/update/{idx}")
    public String update(@ModelAttribute WordDto wordDto, Model model) {
        WordDto word = wordService.update(wordDto);
        model.addAttribute("word", word);
        return "redirect:/Admin/Word";
    }

    @GetMapping("/Admin/delete/{idx}")
    public String delete(@PathVariable Integer idx) {
        wordService.delete(idx);
        return "redirect:/Admin/Word";
    }
}
