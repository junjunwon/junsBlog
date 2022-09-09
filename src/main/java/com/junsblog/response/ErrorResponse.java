package com.junsblog.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *     "code" : "400",
 *     "message" : "잘못된 요청입니다",
 *     "validation" : {
 *         "title" : "값을 입력해주세요"
 *
 *     }
 * }
 */
@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    /**
     * 회사나 팀마다 규칙이 조금씩 달라서 표준화가 필요하다.
     */
    private final String code;
    private final String message;
    private final Map<String, String> validation = new HashMap<>(); //초기값이 null이기 때문에.

    public void addValidation(String field, String errorMessage) {
        this.validation.put(field, errorMessage);
    }
}
