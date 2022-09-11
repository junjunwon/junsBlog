package com.junsblog.response;

import lombok.Builder;
import lombok.Getter;

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
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY) //null이 들어간 응답값은 response에서 제거해주는 어노테이션
public class ErrorResponse {
    /**
     * 회사나 팀마다 규칙이 조금씩 달라서 표준화가 필요하다.
     */
    private final String code;
    private final String message;
    private final Map<String, String> validation; //초기값이 null이기 때문에.

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public void addValidation(String field, String errorMessage) {
        this.validation.put(field, errorMessage);
    }
}
