package com.project.gongchalkka.global.exception;


import lombok.Getter;

/**
 * BusinessErrorException 클래스
 * 비즈니스 로직에 어긋날 때
 * 400, 409 둥
 *
 * */
@Getter
public class BusinessErrorException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessErrorException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
