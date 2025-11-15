package com.project.gongchalkka.global.exception;


import lombok.Getter;

/**
 * EntityNotFoundError
 * 리소스를 찾을 수 없을 때 (HTTP 404)
 */

@Getter
public class EntityNotFoundErrorException extends RuntimeException {

    private final ErrorCode errorCode;

    public EntityNotFoundErrorException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
