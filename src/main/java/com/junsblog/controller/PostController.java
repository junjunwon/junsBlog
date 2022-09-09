package com.junsblog.controller;

import com.junsblog.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PostController  {


    //등록
    @PostMapping("/savePostJson")
    public Map<String, String> saveJson(@RequestBody @Valid PostCreate params){
        log.info("params = {}", params.toString());

        return Map.of();
    }

    @PostMapping("/savePost")
    public String save(@RequestParam Map<String, String> params) {
        log.info("params = {}", params);
        return "save";
    }

    @GetMapping("/posts")
    public String get() {
        return "Hello World";
    }
}
