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
    public Map<String, String> saveJson(@RequestBody @Valid PostCreate params, BindingResult result){
        log.info("params = {}", params.toString());

        if(result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);
            String fieldName = firstFieldError.getField();
            String errorMessage = firstFieldError.getDefaultMessage();

            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMessage);

            return error;

        }

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
