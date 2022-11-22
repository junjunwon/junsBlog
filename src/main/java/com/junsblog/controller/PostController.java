package com.junsblog.controller;

import com.junsblog.request.PostCreate;
import com.junsblog.request.PostEdit;
import com.junsblog.request.PostSearch;
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
     * 게시글 여러개 조회하는 API (JPA)
     *
     * @author jh.won
     * @since 222.09.10
     * @return
     */
    @GetMapping("/getPageListByJpa")
    public List<PostResponse> getPageListByJpa(Pageable page) {
        return postService.getPageListByJpa(page);
    }

    /**
     * 게시글 여러개 조회하는 API (QueryDsl)
     *
     * @author jh.won
     * @since 222.09.10
     * @return
     */
    @GetMapping("/postsByPage")
    public List<PostResponse> getPageList(@ModelAttribute PostSearch postSearch) {
        return postService.getPageList(postSearch);
    }


    //등록
    @PostMapping("/posts")
    public Map<String, String> saveJson(@RequestBody @Valid PostCreate request){
        request.validate();

        postService.write(request);
        return Map.of();
    }

    @PostMapping("/savePost")
    public String save(@RequestParam Map<String, String> params) {
        log.info("params = {}", params);
        return "save";
    }

    //수정에 관한 것은 응답으로 json을 잘 주지 않는 편이다.
    @PatchMapping("/edit/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId, request);
    }

    @DeleteMapping("/delete/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }

}
