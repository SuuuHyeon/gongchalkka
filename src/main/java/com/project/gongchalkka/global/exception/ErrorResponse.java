package com.project.gongchalkka.global.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

    private final int status;
    private final String code;
    private final String message;


    public ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getHttpStatus().value();
        this.code = errorCode.getErrorCode();
        this.message = errorCode.getMessage();
    }

    public ErrorResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
