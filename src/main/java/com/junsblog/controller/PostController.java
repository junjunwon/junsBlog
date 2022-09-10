package com.junsblog.controller;

import com.junsblog.request.PostCreate;
import com.junsblog.response.PostResponse;
import com.junsblog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    /**
     * 게시글 여러개 조회하는 API
     *
     * @author jh.won
     * @since 222.09.10
     * @return
     */
    @GetMapping("/posts")
    public List<PostResponse> getList() {
        return postService.getList();
    }

    /**
     * 게시글 여러개 조회하는 API
     *
     * @author jh.won
     * @since 222.09.10
     * @return
     */
    @GetMapping("/postsByPage")
    public List<PostResponse> getPageList(Pageable page) {
        return postService.getPageList(page);
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
