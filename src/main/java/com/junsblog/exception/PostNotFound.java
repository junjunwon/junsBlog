package com.junsblog.exception;

/**
 * status 404
 */
public class PostNotFound extends RootException {

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    /**
     * 생성자 오버로딩
     */
    public PostNotFound() {
        super(MESSAGE);
    }

    public PostNotFound(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
