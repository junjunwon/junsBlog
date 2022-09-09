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
    @DisplayName("Posts 요청 시 Hello World를 출력한다.")
    public void test() throws Exception {
        //expected
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(print());
    }

    @Test
    @DisplayName("등록 요청, APPLICATION_FORM_URLENCODED type")
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
    @DisplayName("등록 요청 json type")
    public void saveJson() throws Exception {
        mockMvc.perform(post("/savePostJson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"제목입니다.\", \"content\":\"내용입니다\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("save"))
                .andDo(print());
    }

    @Test
    @DisplayName("saveJon요청 시 title값은 필수다.")
    public void saveJsonValidate() throws Exception {
        mockMvc.perform(post("/savePostJson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":null, \"content\":\"내용입니다\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }


}