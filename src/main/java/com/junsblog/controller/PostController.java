package com.junsblog.controller;

import com.junsblog.domain.Post;
import com.junsblog.request.PostCreate;
import com.junsblog.response.PostResponse;
import com.junsblog.service.PostService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PostController  {


    private final PostService postService;

    /**
     * /posts -> 글 전체 조회(검색+페이징)
     * /posts/{postId} -> 글 한개만 조회
     * @return
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id) {

        PostResponse postResponse = postService.get(id);

        return postResponse;
    }

    //등록
    @PostMapping("/savePostJson")
    public Map<String, String> saveJson(@RequestBody @Valid PostCreate request){
        log.info("params = {}", request.toString());
        postService.write(request);
        return Map.of();
    }

    @PostMapping("/savePost")
    public String save(@RequestParam Map<String, String> params) {
        log.info("params = {}", params);
        return "save";
    }

}
