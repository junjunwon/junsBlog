package com.junsblog.exception;

import lombok.Getter;

/**
 * status : 400
 */
@Getter
public class InvalidRequest extends RootException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    /**
     * 생성자 오버로딩
     */
    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    public InvalidRequest(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
