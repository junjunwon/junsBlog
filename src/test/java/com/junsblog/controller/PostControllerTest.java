package com.junsblog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("등록 요청")
    public void save() throws Exception {
        mockMvc.perform(post("/savePost")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "글 제목입니다.")
                 .param("content", "글 내용입니다."))
                .andExpect(status().isOk())
                .andExpect(content().string("save"))
                .andDo(print());
    }

    @Test
    @DisplayName("Posts 요청 시 Hello World를 출력한다.")
    public void test() throws Exception {
        //expected
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(print());
    }
}