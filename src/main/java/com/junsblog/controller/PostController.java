package com.junsblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PostController  {


    //등록
    @PostMapping("/savePost")
    public String save(@RequestParam String title, @RequestParam String content) {
        log.info("title = {}, content = {}", title, content);
        return "save";
    }

    @GetMapping("/posts")
    public String get() {
        return "Hello World";
    }
}
