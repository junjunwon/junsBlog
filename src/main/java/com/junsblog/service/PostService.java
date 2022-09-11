package com.junsblog.service;

import com.junsblog.domain.Post;
import com.junsblog.domain.PostEditor;
import com.junsblog.exception.PostNotFound;
import com.junsblog.repository.PostRepository;
import com.junsblog.request.PostCreate;
import com.junsblog.request.PostEdit;
import com.junsblog.request.PostSearch;
import com.junsblog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(PostNotFound::new);

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

    public List<PostResponse> getPageList(PostSearch postSearch) {
        return postRepository.getPageList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPageListByJpa(Pageable page) {
        //web에서 page를 1로 가져오면 0으로 변경된다.
        return postRepository.findAll(page).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();

        PostEditor postEditor = postEditorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
    }


    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }
}
