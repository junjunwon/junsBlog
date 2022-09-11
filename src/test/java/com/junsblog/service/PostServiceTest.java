package com.junsblog.service;

import com.junsblog.domain.Post;
import com.junsblog.exception.PostNotFound;
import com.junsblog.repository.PostRepository;
import com.junsblog.request.PostCreate;
import com.junsblog.request.PostEdit;
import com.junsblog.request.PostSearch;
import com.junsblog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.by;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void testWrite() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postService.write(postCreate);

        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다." , post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 한개 조회")
    void findById() {
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        Long postId = 1L;

        //when
        PostResponse getPost = postService.get(post.getId());

        //then
        assertNotNull(getPost);
        assertEquals(1L, postRepository.count());
        assertEquals("foo" , getPost.getTitle());
        assertEquals("bar", getPost.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회 Test")
    void findAll() throws Exception {
        //given
        postRepository.saveAll(List.of(
            Post.builder()
                    .title("foo")
                    .content("bar")
                    .build(),
            Post.builder()
                .title("foo2")
                .content("bar2")
                .build()
        ));

        //when
        List<PostResponse> responseList = postService.getList();

        //then
        assertNotNull(responseList);
        assertEquals(2L, responseList.size());

    }

    @Test
    @DisplayName("글 1페이지 조회 JPA")
    void findByPageByJpa() {
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("나만의 블로그 제목 - " + i)
                            .content("나만의 글 목록 - "+ i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        //jpaRepository로 pageable을 넘겨줄 때
        Pageable pageable = PageRequest.of(0, 5, by(DESC, "id"));
//        Pageable pageable = PageRequest.of(0, 5);

        //when
        List<PostResponse> responseList = postService.getPageListByJpa(pageable);

        //then
        assertNotNull(responseList);
        assertEquals(5L, responseList.size());
        assertEquals("나만의 블로그 제목 - 30", responseList.get(0).getTitle());
        assertEquals("나만의 글 목록 - 30", responseList.get(0).getContent());
        assertEquals("나만의 글 목록 - 26", responseList.get(4).getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회 QueryDSL")
    void findByPage() {
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("나만의 블로그 제목 - " + i)
                            .content("나만의 글 목록 - "+ i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        //when
        List<PostResponse> responseList = postService.getPageList(postSearch);

        //then
        assertNotNull(responseList);
        assertEquals(10L, responseList.size());
        assertEquals("나만의 블로그 제목 - 30", responseList.get(0).getTitle());
        assertEquals("나만의 글 목록 - 30", responseList.get(0).getContent());
        assertEquals("나만의 글 목록 - 26", responseList.get(4).getContent());
    }

    @Test
    @DisplayName("글 제목 수정")
    void editTitle() {
        //given
        Post post = Post.builder()
                .title("준호의 꿈")
                .content("너는 아니?")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("준호의 절망") // 이렇게 할 경우 content = null이 들어가지만, 클라이언트와 협의해서 content원본을 보낼 수도 있고 안보낼 수도 있다.
                .content("너는 아니?")
                .build();
        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post getPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id = " + post.getId()));

        assertEquals(postEdit.getTitle(), getPost.getTitle());
        assertEquals("너는 아니?", getPost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void editContent() {
        //given
        Post post = Post.builder()
                .title("준호의 꿈")
                .content("너는 아니?")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("준호의 꿈") // 이렇게 할 경우 content = null이 들어가지만, 클라이언트와 협의해서 content원본을 보낼 수도 있고 안보낼 수도 있다.
                .content("나는 몰라")
                .build();
        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post getPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id = " + post.getId()));

        assertEquals(postEdit.getTitle(), getPost.getTitle());
        assertEquals(postEdit.getContent(), getPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void delete() {
        //given
        Post post = Post.builder()
                .title("준호의 꿈")
                .content("너는 아니?")
                .build();
        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then
        assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("글 한개 조회 실패에 대한 에러 테스트")
    void failTestForfindById() {
        //given
        Post post = Post.builder()
                .title("준호")
                .content("냠냠 남양주")
                .build();
        postRepository.save(post);

        // expected
        //어떤 예외가 발생했는지에 대한 검증
        assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("게시글 삭제 - 실패에 대한 에러 테스트")
    void failTestFordelete() {
        //given
        Post post = Post.builder()
                .title("준호의 꿈")
                .content("너는 아니?")
                .build();
        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then
        assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("글 제목 수정 - 실패에 대한 에러 테스트")
    void failTestForeditTitle() {
        //given
        Post post = Post.builder()
                .title("준호의 꿈")
                .content("너는 아니?")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("준호의 절망") // 이렇게 할 경우 content = null이 들어가지만, 클라이언트와 협의해서 content원본을 보낼 수도 있고 안보낼 수도 있다.
                .content("너는 아니?")
                .build();
        //when
        postService.edit(post.getId(), postEdit);

        //then
        assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1L, postEdit);
        });
    }
}