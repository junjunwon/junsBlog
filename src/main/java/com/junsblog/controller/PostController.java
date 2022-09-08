package com.junsblog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController  {

    //등록
    @PostMapping("/savePost")
    public String save() {
        return "save";
    }

    @GetMapping("/posts")
    public String get() {
        return "Hello World";
    }
}
