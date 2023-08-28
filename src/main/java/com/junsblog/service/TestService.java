package com.junsblog.service;

import com.junsblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TestService {

    private final PostRepository postRepository;

    public void test() {
        System.out.println("test");
    }

    @Transactional(readOnly = true)
    public void getTestEntity() {
        postRepository.findByTitleAndContentAndSummaryAndHateCountAndLoveCount("title", "content", "summary", 1, 1);
        System.out.println("getTestEntity");
    }
}
