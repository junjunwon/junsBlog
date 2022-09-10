package com.junsblog.service;

import com.junsblog.domain.Post;
import com.junsblog.repository.PostRepository;
import com.junsblog.request.PostCreate;
import com.junsblog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        //postCreate -> entity
        Post post = Post.builder()
                            .title(postCreate.getTitle())
                            .content(postCreate.getContent())
                            .build();

        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        //요구사항이 들어오면 응답 클래스로 분리하세요!!
        //json응답에서 title값 길이를 최대 10글자로 해주세요.
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

    }

    public List<PostResponse> getList() {
        return postRepository.findAll().stream()
                .map(PostResponse::new).collect(Collectors.toList());

    }

    public List<PostResponse> getPageList(Pageable page) {
        //web에서 page를 1로 가져오면 0으로 변경된다.

        return postRepository.findAll(page).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
}
