package com.junsblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junsblog.domain.Post;
import com.junsblog.repository.PostRepository;
import com.junsblog.request.PostCreate;
import com.junsblog.request.PostEdit;
import com.junsblog.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest //web layoer 테스트를 할때는 괜찮지만 애플리케이션 전반적인 테스트시 적합하진 않다
@SpringBootTest //mockMvc 주입이 안된다 -> @autoConfigureMockMvc
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    void clean() {
        // 각각의 메소드들이 실행되기 전에 실행되는 함수.
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("등록 요청, APPLICATION_FORM_URLENCODED type")
    public void save() throws Exception {
        mockMvc.perform(post("/savePost")
                .contentType(APPLICATION_FORM_URLENCODED)
                        .param("title", "글 제목입니다.")
                        .param("content", "글 내용입니다."))
                .andExpect(status().isOk())
                .andExpect(content().string("save"))
                .andDo(print());
    }

    @Test
    @DisplayName("등록 요청 json type")
    public void saveJson() throws Exception {
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\":\"제목입니다.\", \"content\":\"내용입니다\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(print());
    }

//    @Test
//    @DisplayName("saveJon요청 시 title값은 필수다.")
//    public void saveJsonValidate() throws Exception {
//        mockMvc.perform(post("/posts")
//                        .contentType(APPLICATION_JSON)
//                        .content("{\"title\":null, \"content\":\"내용입니다\"}"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
//                .andDo(print());
//    }

    @Test
    @DisplayName("post 등록 요청 시 데이터베이스에 저장된다.")
    public void saveDatabase() throws Exception {
        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content ("{\"title\":\"제목입니다.\", \"content\":\"내용입니다.\"}"))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다." , post.getTitle());
        assertEquals("내용입니다.", post.getContent());

    }

    @Test
    @DisplayName("objectMapper && Builder 패턴을 사용하여 DB에 저장")
    void saveDatabaseByObjectMappingAndBuilder() throws Exception {
//        PostCreate request = new PostCreate("제목입니다.", "내용입니다.");
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);//실무에서 정말 많이 사용함

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content ("{\"title\":\"제목입니다.\", \"content\":\"내용입니다.\"}"))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다." , post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 한개 조회")
    void findById() throws Exception {

        //given
        Long postId = 1L;
        Post post = Post.builder()
                .title("2345643")
                .content("bar")
                .build();

        postRepository.save(post);

        //expected ( when + then )
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andDo(print());

    }

    @Test
    @DisplayName("글 여러개 조회 Test")
    void findAll() throws Exception {
        //given
        Post post = postRepository.save(Post.builder()
                .title("title1")
                .content("bar1")
                .build());

        Post post2 = postRepository.save(Post.builder()
                .title("title2")
                .content("bar2")
                .build());


        /**
         * jsonPath에 list를 위한 처리가 필요하다.
         */
        //expected
        mockMvc.perform(get("/posts"))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id").value(post.getId()))
                .andExpect(jsonPath("$[0].title").value("title1"))
                .andExpect(jsonPath("$[0].content").value("bar1"))
                .andExpect(jsonPath("$[1].id").value(post2.getId()))
                .andExpect(jsonPath("$[1].title").value("title2"))
                .andExpect(jsonPath("$[1].content").value("bar2"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회 페이징처리 Jpa")
    void findByPageByJpa() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("나만의 블로그 제목 - " + i)
                            .content("나만의 글 목록 - "+ i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);


        /**
         * jsonPath에 list를 위한 처리가 필요하다.
         */
        //expected
        mockMvc.perform(get("/getPageListByJpa?page=1&sort=id,desc")) //applicaiton.yml에 default size를 설정해주지 않으면 &size=5로 파라미터를 넘겨주면 된다.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$[0].title").value("나만의 블로그 제목 - 30"))
                .andExpect(jsonPath("$[0].content").value("나만의 글 목록 - 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회 페이징처리 QueryDsl")
    void findByPage() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("나만의 블로그 제목 - " + i)
                            .content("나만의 글 목록 - "+ i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);


        /**
         * jsonPath에 list를 위한 처리가 필요하다.
         */
        //expected
        mockMvc.perform(get("/postsByPage?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("나만의 블로그 제목 - 30"))
                .andExpect(jsonPath("$[0].content").value("나만의 글 목록 - 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void editTitle() throws Exception {
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

        //expected
        mockMvc.perform(patch("/edit/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("준호의 꿈"))
//                .andExpect(jsonPath("$.content").value("나는 몰라"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 삭제")
    void deleteTest() throws Exception {
        //given
        Post post = Post.builder()
                .title("준호의 꿈")
                .content("너는 아니?")
                .build();
        postRepository.save(post);

        //expected
        mockMvc.perform(delete("/delete/{postId}", post.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 한개 조회 - 실패에 대한 에러 테스트")
    void failTestForfindById() throws Exception {

        mockMvc.perform(delete("/delete/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("글 제목 수정 - 실패에 대한 에러 테스트")
    void failTestForeditTitle() throws Exception {
        //given
        Post post = Post.builder()
                .title("준호의 꿈")
                .content("너는 아니?")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("준호의 절망")
                .content("너는 아니?")
                .build();

        //expected
        mockMvc.perform(patch("/edit/{postId}", post.getId() + 1)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("post 등록 요청 시 바보는 포함될 수 없다.(잘못된 요청) -> 데이터베이스에 저장된다.")
    public void exceptionTestForsaveDatabase() throws Exception {
        //given
        PostCreate postCreate = PostCreate.builder()
                        .title("나는 바보입니다.")
                        .content("내용입니다.")
                        .build();

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreate)))
                .andExpect(status().isBadRequest())
                .andDo(print());

//        //then
//        assertEquals(1L, postRepository.count());
//
//        Post post = postRepository.findAll().get(0);
//        assertEquals("제목입니다." , post.getTitle());
//        assertEquals("내용입니다.", post.getContent());

    }
}
